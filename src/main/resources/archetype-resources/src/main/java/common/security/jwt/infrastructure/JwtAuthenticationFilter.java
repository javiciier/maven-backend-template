package ${package}.common.security.jwt.infrastructure;

import ${package}.common.security.jwt.application.JwtGenerator;
import ${package}.common.security.jwt.domain.JwtData;
import ${package}.users.domain.entities.roles.RoleNames;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static ${package}.common.security.SecurityConstants .*;

@Log4j2
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

  private final JwtGenerator jwtGenerator;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtGenerator jwtGenerator) {
    super(authenticationManager);
    this.jwtGenerator = jwtGenerator;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    if (!canApplyFilter(request)) {
      chain.doFilter(request, response);
      return;
    }

    // Extract JWT content and inject its claims into the Spring Security context
    UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    // Continue filtering requests
    chain.doFilter(request, response);
  }

  // region AUXILIAR METHODS
  private boolean canApplyFilter(HttpServletRequest request) {
    // Check if request has a Authorization header
    String authHeaderValue = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authHeaderValue == null) {
      log.warn("Received request without JWT in the Authorization header");
      return false;
    }

    // Check if the value of the header is a Bearer token
    boolean containsBearerToken = authHeaderValue.startsWith(PREFIX_BEARER_TOKEN);
    if (!containsBearerToken) {
      log.warn("Received invalid Bearer token: {}", authHeaderValue);
      return false;
    }

    return true;
  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    // Remove "Bearer " from header
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    String token = authHeader.replace(PREFIX_BEARER_TOKEN, "");

    // Parse JWT content
    JwtData data = jwtGenerator.extractData(token);
    if (data == null) {
      return null;
    }

    // Add user data to the security context
    request.setAttribute(TOKEN_ATTRIBUTE_NAME, token);
    request.setAttribute(USER_ID_ATTRIBUTE_NAME, data.getUserID());

    // Set user roles
    Set<GrantedAuthority> authorities = createAuthorities(data);

    return new UsernamePasswordAuthenticationToken(data, null, authorities);
  }

  private Set<GrantedAuthority> createAuthorities(JwtData token) {
    log.debug("Registering granted authorities for user with ID '{}'", token.getUserID());

    boolean hasRoles = (token.getRoles() != null) && (!token.getRoles().isEmpty());
    Set<GrantedAuthority> authorities = new HashSet<>(token.getRoles().size());

    if (!hasRoles) {
      return authorities;
    }

    for (RoleNames role: token.getRoles()) {
      String roleName = role.getName().replace("\"", "");
      SimpleGrantedAuthority authority = new SimpleGrantedAuthority(ROLE_ATTRIBUTE_NAME + roleName);
      authorities.add(authority);
    }

    log.debug("Registered granted authorities succesfuly for user with ID '{}': {}", token.getUserID(), authorities);
    return authorities;
  }

  // endregion AUXILIAR METHODS

}
