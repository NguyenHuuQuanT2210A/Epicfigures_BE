package com.example.orderService.validator;

import com.example.orderService.config.FutureTimeWithReference;
import com.example.orderService.dto.OrderDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;

public class FutureTimeWithReferenceValidator implements ConstraintValidator<FutureTimeWithReference, OrderDTO> {
    private LocalDate referenceDateTime;
    @Override
    public void initialize(FutureTimeWithReference constraintAnnotation) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        this.referenceDateTime = LocalDate.parse(constraintAnnotation.reference(), formatter);
    }

    @Override
    public boolean isValid(OrderDTO orderDTO, ConstraintValidatorContext context) {
        if (orderDTO.getStartDate() == null || orderDTO.getEndDate() == null) {
            return true;
        }
        return orderDTO.getEndDate().isAfter(orderDTO.getStartDate());
    }
}
