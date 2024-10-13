package $package.common.config.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.*;

import $package.common.config.serialization.serializers.*;
import $package.common.config.serialization.deserializers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Configures the object mappers used by the application
 */
@Configuration
public class ObjectMapperConfiguration {
    public static JavaTimeModule configureTimeModules() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // Serializers
        javaTimeModule.addSerializer(LocalDateTime.class, new JacksonLocalDateTimeSerializer());
        javaTimeModule.addSerializer(LocalDate.class, new JacksonLocalDateSerializer());

        // Deserializers
        javaTimeModule.addDeserializer(LocalDate.class, new JacksonLocalDateDeserializer());

        return javaTimeModule;
    }

    @Bean
    @Primary
    public static ObjectMapper configureObjectMapper() {
        JavaTimeModule javaTimeModule = configureTimeModules();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(javaTimeModule);

        return objectMapper;
    }

}
