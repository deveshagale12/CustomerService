package com.CustomerService.exception;

public class InvalidCustomerStatusException extends RuntimeException {
    public InvalidCustomerStatusException(String message) {
        super(message);
    }
}
