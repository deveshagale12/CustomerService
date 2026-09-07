package com.CustomerService.dto;

import com.CustomerService.entity.CustomerStatus;
import jakarta.validation.constraints.NotNull;


public class CustomerStatusUpdateDto {

    @NotNull(message = "status is required")
    private CustomerStatus status;

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }
}
