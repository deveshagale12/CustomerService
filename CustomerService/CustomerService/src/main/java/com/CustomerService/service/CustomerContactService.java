package com.CustomerService.service;

import com.CustomerService.dto.CustomerContactDto;
import com.CustomerService.entity.CustomerContact;
import com.CustomerService.exception.ContactNotFoundException;
import com.CustomerService.exception.CustomerNotFoundException;
import com.CustomerService.repository.CustomerContactRepository;
import com.CustomerService.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerContactService {

    private final CustomerContactRepository contactRepository;
    private final CustomerRepository customerRepository;

    public CustomerContactService(CustomerContactRepository contactRepository, CustomerRepository customerRepository) {
        this.contactRepository = contactRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerContactDto addContact(Long customerId, CustomerContactDto dto) {
        ensureCustomerExists(customerId);

        CustomerContact contact = new CustomerContact();
        contact.setCustomerId(customerId);
        contact.setContactType(dto.getContactType());
        contact.setContactValue(dto.getContactValue());
        contact.setVerified(false);

        CustomerContact saved = contactRepository.save(contact);
        return toDto(saved);
    }

    public List<CustomerContactDto> getContacts(Long customerId) {
        ensureCustomerExists(customerId);
        return contactRepository.findByCustomerId(customerId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CustomerContactDto verifyContact(Long customerId, Long contactId) {
        CustomerContact contact = findContactOrThrow(customerId, contactId);
        contact.setVerified(true);
        CustomerContact saved = contactRepository.save(contact);
        return toDto(saved);
    }

    @Transactional
    public CustomerContactDto updateContact(Long customerId, Long contactId, CustomerContactDto dto) {
        CustomerContact contact = findContactOrThrow(customerId, contactId);
        contact.setContactType(dto.getContactType());
        contact.setContactValue(dto.getContactValue());
        contact.setVerified(false);
        CustomerContact saved = contactRepository.save(contact);
        return toDto(saved);
    }

    @Transactional
    public void deleteContact(Long customerId, Long contactId) {
        CustomerContact contact = findContactOrThrow(customerId, contactId);
        contactRepository.delete(contact);
    }

    private CustomerContact findContactOrThrow(Long customerId, Long contactId) {
        CustomerContact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ContactNotFoundException("Contact not found with id: " + contactId));
        if (!contact.getCustomerId().equals(customerId)) {
            throw new ContactNotFoundException("Contact " + contactId + " does not belong to customer " + customerId);
        }
        return contact;
    }

    private void ensureCustomerExists(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException("Customer not found with id: " + customerId);
        }
    }

    private CustomerContactDto toDto(CustomerContact contact) {
        CustomerContactDto dto = new CustomerContactDto();
        dto.setContactId(contact.getContactId());
        dto.setContactType(contact.getContactType());
        dto.setContactValue(contact.getContactValue());
        dto.setVerified(contact.isVerified());
        return dto;
    }
}
