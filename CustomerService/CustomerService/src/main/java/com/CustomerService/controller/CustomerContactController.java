package com.CustomerService.controller;

import com.example.customerservice.dto.CustomerContactDto;
import com.example.customerservice.service.CustomerContactService;
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


@RestController
@RequestMapping("/api/v1/customers/{customerId}/contacts")
public class CustomerContactController {

    private final CustomerContactService contactService;

    public CustomerContactController(CustomerContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<CustomerContactDto> addContact(
            @PathVariable Long customerId, @Valid @RequestBody CustomerContactDto dto) {
        CustomerContactDto response = contactService.addContact(customerId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CustomerContactDto>> getContacts(@PathVariable Long customerId) {
        return ResponseEntity.ok(contactService.getContacts(customerId));
    }

    @PutMapping("/{contactId}")
    public ResponseEntity<CustomerContactDto> updateContact(
            @PathVariable Long customerId, @PathVariable Long contactId, @Valid @RequestBody CustomerContactDto dto) {
        return ResponseEntity.ok(contactService.updateContact(customerId, contactId, dto));
    }

    @PatchMapping("/{contactId}/verify")
    public ResponseEntity<CustomerContactDto> verifyContact(
            @PathVariable Long customerId, @PathVariable Long contactId) {
        return ResponseEntity.ok(contactService.verifyContact(customerId, contactId));
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long customerId, @PathVariable Long contactId) {
        contactService.deleteContact(customerId, contactId);
        return ResponseEntity.noContent().build();
    }
}
