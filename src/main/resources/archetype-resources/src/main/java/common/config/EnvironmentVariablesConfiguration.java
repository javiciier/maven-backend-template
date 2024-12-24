package ${package}.common.config;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import jakarta.annotation.PostConstruct;
import java.io.File;

@Log4j2
@Getter
@Configuration
@PropertySource(
        value = "file:${user.dir}/.${spring.profiles.active}.env",
        ignoreResourceNotFound = true
)
public class EnvironmentVariablesConfiguration {
    private static final String ENV_FILE_PATH = "${user.dir}/.${spring.profiles.active}.env";

    @PostConstruct
    public void init() {
        File envFile = new File(ENV_FILE_PATH);
        if (!envFile.exists()) {
            log.warn("The configuration file '{}' was not found. Environmental variables will be loaded from default settings.", envFile.getPath());
        } else {
            log.info("Environmental configuration loaded from '{}'.", ENV_FILE_PATH);
        }
    }
}
