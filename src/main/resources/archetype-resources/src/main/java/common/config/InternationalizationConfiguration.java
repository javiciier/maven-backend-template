package ${package}.common.config;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

/**
 * Configures aspects related with I18N and translations
 */
@Configuration
public class InternationalizationConfiguration {

  private static final Locale DEFAULT_LOCALE = Locale.of("es");
  private static final String[] supportedLanguages = {"es", "en"};
  private static final String[] supportedElements = {"exceptions", "fields", "validations"};
  private static final String CLASSPATH = "i18n";

  @Bean
  public static MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());

    for (String basename : createBasenames()) {
      messageSource.addBasenames(basename);
    }

    return messageSource;
  }

  // INFO: https://lokalise.com/blog/spring-boot-internationalization
  @Bean
  public static LocaleResolver localeResolver() {
    List<Locale> supportedLocales = getSupportedLocales();

    AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
    localeResolver.setSupportedLocales(supportedLocales);
    localeResolver.setDefaultLocale(DEFAULT_LOCALE);

    return localeResolver;
  }


  private static List<String> createBasenames() {
    List<String> baseNamePaths = new ArrayList<>();

    for (String lang : supportedLanguages) {
      for (String item : supportedElements) {
        String path = Path.of(CLASSPATH, lang, item).toUri().toString();
        baseNamePaths.add(path);
      }
    }

    return baseNamePaths;
  }

  private static List<Locale> getSupportedLocales() {
    return Arrays.stream(supportedLanguages)
        .map(Locale::of)
        .toList();
  }
}
