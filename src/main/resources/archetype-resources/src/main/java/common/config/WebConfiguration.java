package ${package}.common.config;

import ${package}.common.annotations.validations.CheckUserIdentityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
  // region DEPENDENCIES
  private final CheckUserIdentityInterceptor checkUserIdentityInterceptor;
  // endregion DEPENDENCIES

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(checkUserIdentityInterceptor);
  }
}
