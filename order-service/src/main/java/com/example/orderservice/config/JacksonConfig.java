package com.example.orderservice.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.featuresToEnable(SerializationFeature.INDENT_OUTPUT);
        builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        SimpleModule module = new SimpleModule();
//        module.addSerializer(BigDecimal.class, new CustomBigDecimalSerializer());
        module.addSerializer(LocalDateTime.class, new CustomLocalDateTimeSerializer());
        module.addDeserializer(LocalDateTime.class, new CustomLocalDateTimeDeserializer());

        builder.modules(new JavaTimeModule(), module);

        return builder;
    }

//    public static class CustomBigDecimalSerializer extends JsonSerializer<BigDecimal> {
//        @Override
//        public void serialize(BigDecimal value, JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws IOException {
//            DecimalFormat df = new DecimalFormat("#,###.00");
//            String formattedValue = df.format(value);
//            gen.writeString(formattedValue);
//        }
//    }

    public static class CustomLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedDateTime = value.format(formatter);
            gen.writeString(formattedDateTime);
        }
    }

    public static class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String dateString = p.getText();
            DateTimeFormatter formatter;

            if (dateString.contains("T")) {
                formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            } else {
                formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            }
            return LocalDateTime.parse(p.getText(), formatter);
        }
    }
}