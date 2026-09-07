package com.CustomerService.exception;
public class DuplicateMobileNumberException extends RuntimeException {
    public DuplicateMobileNumberException(String message) {
        super(message);
    }
}