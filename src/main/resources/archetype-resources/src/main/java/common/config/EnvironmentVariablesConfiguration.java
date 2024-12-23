package ${package}.common.config;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Log4j2
@Getter
@Configuration
@PropertySource("file:${user.dir}/.${spring.profiles.active}.env")
public class EnvironmentVariablesConfiguration {
    static {
        log.info("Loading environmental configuration...");
    }

}
