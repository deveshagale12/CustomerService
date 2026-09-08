package com.CustomerService.controller;

import com.CustomerService.dto.CustomerAddressDto;
import com.CustomerService.service.CustomerAddressService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequestMapping("/api/v1/customers/{customerId}/addresses")
public class CustomerAddressController {

    private final CustomerAddressService addressService;

    public CustomerAddressController(CustomerAddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<CustomerAddressDto> addAddress(
            @PathVariable Long customerId, @Valid @RequestBody CustomerAddressDto dto) {
        CustomerAddressDto response = addressService.addAddress(customerId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CustomerAddressDto>> getCustomerAddresses(@PathVariable Long customerId) {
        return ResponseEntity.ok(addressService.getCustomerAddresses(customerId));
    }

    @GetMapping("/primary")
    public ResponseEntity<CustomerAddressDto> getPrimaryAddress(@PathVariable Long customerId) {
        return ResponseEntity.ok(addressService.getPrimaryAddress(customerId));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<CustomerAddressDto> updateAddress(
            @PathVariable Long customerId, @PathVariable Long addressId, @Valid @RequestBody CustomerAddressDto dto) {
        return ResponseEntity.ok(addressService.updateAddress(customerId, addressId, dto));
    }

    @PatchMapping("/{addressId}/set-primary")
    public ResponseEntity<CustomerAddressDto> setPrimaryAddress(
            @PathVariable Long customerId, @PathVariable Long addressId) {
        return ResponseEntity.ok(addressService.setPrimaryAddress(customerId, addressId));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long customerId, @PathVariable Long addressId) {
        addressService.deleteAddress(customerId, addressId);
        return ResponseEntity.noContent().build();
    }
}
