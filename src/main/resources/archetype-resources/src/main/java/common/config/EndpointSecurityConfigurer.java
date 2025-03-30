package ${package}.common.config;

import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.context.annotation.Configuration;

/**
 * Interface to indicate a RestController that it must configure who can access each of the
 * endpoints it is exposing
 */
@Configuration
public interface EndpointSecurityConfigurer {

  /**
   * Allows or denies requests to the endpoints
   */
  public void secureEndpoints(HttpSecurity httpSecurity) throws Exception;
}
