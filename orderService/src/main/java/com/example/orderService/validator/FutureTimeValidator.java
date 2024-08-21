package com.example.orderService.validator;

import com.example.orderService.config.FutureTime;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FutureTimeValidator implements ConstraintValidator<FutureTime, LocalDate> {
    private LocalDate referenceDateTime;
    @Override
    public void initialize(FutureTime constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null values are valid, you can change this based on your requirements
        }
        return value.isAfter(LocalDate.now());
    }
}
