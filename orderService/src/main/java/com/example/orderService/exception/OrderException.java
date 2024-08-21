package com.example.orderService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class OrderException extends RuntimeException{

    public OrderException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    private final HttpStatus status;



    public HttpStatus getStatus() {
        return status;
    }
}
