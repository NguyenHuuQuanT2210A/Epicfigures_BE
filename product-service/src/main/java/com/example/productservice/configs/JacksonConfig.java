package com.example.productservice.configs;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());

        SimpleModule module = new SimpleModule();
//        module.addSerializer(BigDecimal.class, new CustomBigDecimalSerializer());
        module.addSerializer(LocalDateTime.class, new CustomLocalDateTimeSerializer());
        mapper.registerModule(module);

        return mapper;
    }

//    public static class CustomBigDecimalSerializer extends JsonSerializer<BigDecimal> {
//        @Override
//        public void serialize(BigDecimal value, JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws IOException, IOException {
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
}