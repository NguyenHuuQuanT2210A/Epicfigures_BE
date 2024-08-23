package com.example.userservice.exceptions;

import com.example.userservice.dtos.response.ApiResponse;
import com.example.userservice.securities.jwt.AccessDeniedHandler;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.ForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.management.ServiceNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, String>> handleProductNotFoundException(NotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(cv -> {
            errors.put(cv.getPropertyPath().toString(), cv.getMessage());
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "Invalid value for parameter: " + ex.getName());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ResponseEntity<Map<String, String>> handleAllUncaughtException(Exception ex, WebRequest request) {
//        Map<String, String> errorResponse = new HashMap<>();
//        errorResponse.put("message", "An unexpected error occurred: " + ex.getMessage());
//        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e) {
        e.printStackTrace(); // In ra stack trace cho việc debug
        return ResponseEntity.status(e.getStatus()).body(ApiResponse.builder()
                .code(e.getStatus().value())
                .message("Error : " + e.getMessage())
                .build());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> handleForbiddenException(ForbiddenException e) {
        Logger logger = LoggerFactory.getLogger(AccessDeniedHandler.class);
        logger.error("Unauthorized error: {}", e.getMessage());

        e.printStackTrace(); // In ra stack trace cho việc debug
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: " + e.getMessage());
    }

    @ExceptionHandler(ServiceNotFoundException.class)
    public ResponseEntity<String> handleServiceNotFoundException(ServiceNotFoundException e) {
        e.printStackTrace(); // In ra stack trace cho việc debug
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
    }

    @ExceptionHandler(AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }
}
