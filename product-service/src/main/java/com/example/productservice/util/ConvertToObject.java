package com.example.productservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;

public class ConvertToObject<T> {
    private final ObjectMapper objectMapper;

    public ConvertToObject(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public T convertToObjectResponse(Object object, Class<T> clazz) {
        if (object instanceof LinkedHashMap) {
            return objectMapper.convertValue(object, clazz);
        } else {
            return clazz.cast(object);
        }
    }
}
