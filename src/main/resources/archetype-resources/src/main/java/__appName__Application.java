package ${package};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"group"})
public class ${appName}Application {

  public static void main(String[] args) {
    SpringApplication.run(${appName}Application.class, args);
  }
}
