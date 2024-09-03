package com.example.productservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class RedisRequest {
    String key;
    String value;
    long timeoutInDays;
    String field;
    Object fieldValue;
    List<String> fields;
    List<Object> values;
    String fieldPrefix;
}
