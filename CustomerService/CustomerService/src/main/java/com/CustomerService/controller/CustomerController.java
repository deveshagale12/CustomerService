package com.CustomerService.controller;

import com.CustomerService.dto.CustomerRequestDto;
import com.CustomerService.dto.CustomerResponseDto;
import com.CustomerService.dto.CustomerStatusUpdateDto;
import com.CustomerService.entity.CustomerStatus;
import com.CustomerService.service.CustomerService;
import com.CustomerService.dto.LoginRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<CustomerResponseDto> registerCustomer(@Valid @RequestBody CustomerRequestDto request) {
        CustomerResponseDto response = customerService.registerCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
public ResponseEntity<CustomerResponseDto> login(
        @RequestBody LoginRequestDto request) {

    CustomerResponseDto response =
            customerService.login(request);

    return ResponseEntity.ok(response);
}


    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerResponseDto> getCustomerByEmail(@PathVariable String email) {
        return ResponseEntity.ok(customerService.getCustomerByEmail(email));
    }

    @GetMapping("/mobile/{mobileNumber}")
    public ResponseEntity<CustomerResponseDto> getCustomerByMobile(@PathVariable String mobileNumber) {
        return ResponseEntity.ok(customerService.getCustomerByMobile(mobileNumber));
    }

    @GetMapping("/number/{customerNumber}")
    public ResponseEntity<CustomerResponseDto> getCustomerByNumber(@PathVariable String customerNumber) {
        return ResponseEntity.ok(customerService.getCustomerByNumber(customerNumber));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> getCustomers(
            @RequestParam(required = false) CustomerStatus status) {
        if (status != null) {
            return ResponseEntity.ok(customerService.getCustomersByStatus(status));
        }
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(
            @PathVariable Long customerId, @Valid @RequestBody CustomerRequestDto request) {
        return ResponseEntity.ok(customerService.updateCustomer(customerId, request));
    }

    @PatchMapping("/{customerId}/status")
    public ResponseEntity<CustomerResponseDto> changeCustomerStatus(
            @PathVariable Long customerId, @Valid @RequestBody CustomerStatusUpdateDto statusUpdate) {
        return ResponseEntity.ok(customerService.changeCustomerStatus(customerId, statusUpdate));
    }

    @PatchMapping("/{customerId}/activate")
    public ResponseEntity<CustomerResponseDto> activateCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.activateCustomer(customerId));
    }

    @PatchMapping("/{customerId}/block")
    public ResponseEntity<CustomerResponseDto> blockCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.blockCustomer(customerId));
    }

    @PatchMapping("/{customerId}/close")
    public ResponseEntity<CustomerResponseDto> closeCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.closeCustomer(customerId));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
