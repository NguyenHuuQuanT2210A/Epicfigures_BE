package com.example.productservice.configs;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());

//        SimpleModule module = new SimpleModule();
//        module.addSerializer(BigDecimal.class, new CustomBigDecimalSerializer());
//        mapper.registerModule(module);

        return mapper;
    }

//    public static class CustomBigDecimalSerializer extends JsonSerializer<BigDecimal> {
//        @Override
//        public void serialize(BigDecimal value, JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws IOException, IOException {
//            DecimalFormat df = new DecimalFormat("#,###.##");
//            String formattedValue = df.format(value);
//            gen.writeString(formattedValue);
//        }
//    }

//    @Bean
//    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
//        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
//        builder.featuresToEnable(SerializationFeature.INDENT_OUTPUT);
//        builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        builder.modules(new JavaTimeModule());
//
//        SimpleModule module = new SimpleModule();
//        module.addSerializer(BigDecimal.class, new CustomBigDecimalSerializer());
//        builder.modules(module);
//
//        return builder;
//    }
//
//    public static class CustomBigDecimalSerializer extends JsonSerializer<BigDecimal> {
//        @Override
//        public void serialize(BigDecimal value, JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws IOException, IOException {
//            DecimalFormat df = new DecimalFormat("#,###.##");
//            String formattedValue = df.format(value);
//            gen.writeString(formattedValue);
//        }
//    }
}