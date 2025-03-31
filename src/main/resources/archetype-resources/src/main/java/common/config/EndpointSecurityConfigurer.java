package ${package}.common.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * Interface to indicate a RestController that it must configure who can access each of the
 * endpoints it is exposing
 */
public interface EndpointSecurityConfigurer {

  /**
   * Allows or denies requests to the endpoints
   */
  void secureEndpoints(HttpSecurity httpSecurity) throws Exception;
}
