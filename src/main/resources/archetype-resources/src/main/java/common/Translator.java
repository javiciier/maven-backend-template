package ${package}.common;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class Translator {

  private final MessageSource messageSource;

  public String generateMessage(String exceptionKey, Locale locale) {
    return generateMessage(exceptionKey, null, locale);
  }

  public String generateMessage(String exceptionKey, Object[] args, Locale locale) {
    return messageSource.getMessage(
        exceptionKey,
        args,
        exceptionKey,
        locale
    );
  }
}
