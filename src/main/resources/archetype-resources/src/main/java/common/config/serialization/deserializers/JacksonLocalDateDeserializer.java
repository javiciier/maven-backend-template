package ${package}.common.config.serialization.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JacksonLocalDateDeserializer extends StdDeserializer<LocalDate> {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

  public JacksonLocalDateDeserializer() {
    super(LocalDate.class);
  }

  @Override
  public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException {
    String dateString = jsonParser.getValueAsString();

    return LocalDate.parse(dateString, formatter);
  }
}
