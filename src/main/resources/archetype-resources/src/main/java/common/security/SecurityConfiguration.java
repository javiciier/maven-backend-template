package ${package}.common.security;

import ${package}.common.config.EndpointSecurityConfigurer;
import ${package}.common.security.jwt.application.JwtGenerator;
import ${package}.common.security.jwt.infrastructure.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
  /**
   * List that holds the endpoint access configurations for each Rest controller
   */
  private final List<EndpointSecurityConfigurer> endpointSecurityConfigurersList;

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(
        AuthenticationManagerBuilder.class);
    return authenticationManagerBuilder.build();
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter(
      AuthenticationManager authenticationManager, JwtGenerator jwtGenerator) throws Exception {
    return new JwtAuthenticationFilter(authenticationManager, jwtGenerator);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtGenerator jwtGenerator,
      JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {

    http
        // Disable CSRF as it not being used (CSRF only affects Sessions and Cookies, not JWT)
        .csrf(csrf -> csrf.disable())
        // Do not store user Sessions (Stateless API)
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // Apply JWT custom filtering
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    // Apply the security rules configured by each REST controller
    secureEndpoints(http);

    return http.build();
  }

  private void secureEndpoints(HttpSecurity http) throws Exception {
    for (EndpointSecurityConfigurer configurer: endpointSecurityConfigurersList) {
      configurer.secureEndpoints(http);
    }

    // Deny requests to endpoints that are not explicitly registered
    http.authorizeHttpRequests(
        requests -> requests.anyRequest().denyAll()
    );
  }

}

