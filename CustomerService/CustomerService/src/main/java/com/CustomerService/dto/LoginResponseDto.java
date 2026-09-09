package com.CustomerService.dto;

public class LoginResponseDto {

    private String message;
    private String token;
    private CustomerLoginDetailsDto customer;

    public LoginResponseDto() {
    }

    public LoginResponseDto(
            String message,
            String token,
            CustomerLoginDetailsDto customer) {

        this.message = message;
        this.token = token;
        this.customer = customer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public CustomerLoginDetailsDto getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerLoginDetailsDto customer) {
        this.customer = customer;
    }
}