package com.example.orderService.config;

import com.example.orderService.validator.FutureTimeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FutureTimeValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FutureTimeWithReference {
    String message() default "The date must be in the future";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String reference() default "2000-01-01T00:00:00";
}
