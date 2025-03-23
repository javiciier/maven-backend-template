package ${package}.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderBean {

  @Bean
  public static BCryptPasswordEncoder passwordEncoder() {

    return new BCryptPasswordEncoder();
  }
}
