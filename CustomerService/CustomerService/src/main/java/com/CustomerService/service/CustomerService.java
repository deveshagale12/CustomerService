package com.CustomerService.service;

import com.CustomerService.dto.CustomerRequestDto;
import com.CustomerService.dto.CustomerResponseDto;
import com.CustomerService.dto.CustomerStatusUpdateDto;
import com.CustomerService.entity.Customer;
import com.CustomerService.entity.CustomerStatus;
import com.CustomerService.exception.CustomerNotFoundException;
import com.CustomerService.exception.DuplicateEmailException;
import com.CustomerService.exception.DuplicateMobileNumberException;
import com.CustomerService.exception.InvalidCustomerStatusException;
import com.CustomerService.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.CustomerService.dto.LoginRequestDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.CustomerService.dto.LoginResponseDto;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
private final PasswordEncoder passwordEncoder;

public CustomerService(
        CustomerRepository customerRepository,
        PasswordEncoder passwordEncoder) {

    this.customerRepository = customerRepository;
    this.passwordEncoder = passwordEncoder;
}

public LoginResponseDto login(LoginRequestDto request) {

    Customer customer = customerRepository.findByEmail(request.getEmail())
            .orElseThrow(() ->
                    new CustomerNotFoundException("Invalid email or password"));

    if (!passwordEncoder.matches(
            request.getPassword(),
            customer.getPassword())) {

        throw new CustomerNotFoundException("Invalid email or password");
    }

    if (customer.getStatus() == CustomerStatus.BLOCKED) {
        throw new InvalidCustomerStatusException(
                "Customer account is blocked");
    }

    if (customer.getStatus() == CustomerStatus.CLOSED) {
        throw new InvalidCustomerStatusException(
                "Customer account is closed");
    }

    return new LoginResponseDto(
            "Login successful",
            customer.getCustomerId(),
            customer.getCustomerNumber(),
            customer.getFirstName(),
            customer.getMiddleName(),
            customer.getLastName(),
            customer.getEmail(),
            customer.getMobileNumber(),
            customer.getCustomerType().name(),
            customer.getStatus().name()
    );
}

   @Transactional
public CustomerResponseDto registerCustomer(CustomerRequestDto request) {

    if (customerRepository.existsByEmail(request.getEmail())) {
        throw new DuplicateEmailException(
                "A customer with email '" +
                request.getEmail() +
                "' already exists"
        );
    }

    if (customerRepository.existsByMobileNumber(request.getMobileNumber())) {
        throw new DuplicateMobileNumberException(
                "A customer with mobile number '" +
                request.getMobileNumber() +
                "' already exists"
        );
    }

    Customer customer = new Customer();

    customer.setCustomerNumber(generateCustomerNumber());
    customer.setFirstName(request.getFirstName());
    customer.setMiddleName(request.getMiddleName());
    customer.setLastName(request.getLastName());
    customer.setDateOfBirth(request.getDateOfBirth());
    customer.setGender(request.getGender());
    customer.setEmail(request.getEmail());
    customer.setMobileNumber(request.getMobileNumber());
    customer.setCustomerType(request.getCustomerType());

    // Encrypt password
    customer.setPassword(
            passwordEncoder.encode(request.getPassword())
    );

    customer.setStatus(CustomerStatus.PENDING);

    Customer saved = customerRepository.save(customer);

    return toResponseDto(saved);
}


    public CustomerResponseDto getCustomerById(Long customerId) {
        return toResponseDto(findCustomerOrThrow(customerId));
    }

    public CustomerResponseDto getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with email: " + email));
        return toResponseDto(customer);
    }

    public CustomerResponseDto getCustomerByMobile(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with mobile number: " + mobileNumber));
        return toResponseDto(customer);
    }

    public CustomerResponseDto getCustomerByNumber(String customerNumber) {
        Customer customer = customerRepository.findByCustomerNumber(customerNumber)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with customer number: " + customerNumber));
        return toResponseDto(customer);
    }

    public List<CustomerResponseDto> getCustomersByStatus(CustomerStatus status) {
        return customerRepository.findByStatus(status).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<CustomerResponseDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CustomerResponseDto updateCustomer(Long customerId, CustomerRequestDto request) {
        Customer customer = findCustomerOrThrow(customerId);

        if (!customer.getEmail().equalsIgnoreCase(request.getEmail())
                && customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("A customer with email '" + request.getEmail() + "' already exists");
        }
        if (!customer.getMobileNumber().equals(request.getMobileNumber())
                && customerRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new DuplicateMobileNumberException("A customer with mobile number '" + request.getMobileNumber() + "' already exists");
        }

        customer.setFirstName(request.getFirstName());
        customer.setMiddleName(request.getMiddleName());
        customer.setLastName(request.getLastName());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setGender(request.getGender());
        customer.setEmail(request.getEmail());
        customer.setMobileNumber(request.getMobileNumber());
        customer.setCustomerType(request.getCustomerType());

        Customer saved = customerRepository.save(customer);
        return toResponseDto(saved);
    }

    @Transactional
    public CustomerResponseDto changeCustomerStatus(Long customerId, CustomerStatusUpdateDto statusUpdate) {
        return changeStatus(customerId, statusUpdate.getStatus());
    }

    @Transactional
    public CustomerResponseDto activateCustomer(Long customerId) {
        return changeStatus(customerId, CustomerStatus.ACTIVE);
    }

    @Transactional
    public CustomerResponseDto blockCustomer(Long customerId) {
        return changeStatus(customerId, CustomerStatus.BLOCKED);
    }

    @Transactional
    public CustomerResponseDto closeCustomer(Long customerId) {
        return changeStatus(customerId, CustomerStatus.CLOSED);
    }

    @Transactional
    public void deleteCustomer(Long customerId) {
        Customer customer = findCustomerOrThrow(customerId);
        customerRepository.delete(customer);
    }

    private CustomerResponseDto changeStatus(Long customerId, CustomerStatus newStatus) {
        Customer customer = findCustomerOrThrow(customerId);
        validateStatusTransition(customer.getStatus(), newStatus);
        customer.setStatus(newStatus);
        Customer saved = customerRepository.save(customer);
        return toResponseDto(saved);
    }

    private void validateStatusTransition(CustomerStatus current, CustomerStatus target) {
        if (current == CustomerStatus.CLOSED) {
            throw new InvalidCustomerStatusException("Cannot change status of a CLOSED customer");
        }
        if (current == target) {
            throw new InvalidCustomerStatusException("Customer is already in status " + target);
        }
    }

    private Customer findCustomerOrThrow(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
    }

    private String generateCustomerNumber() {
        return "CUS" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }

    private CustomerResponseDto toResponseDto(Customer customer) {
        CustomerResponseDto dto = new CustomerResponseDto();
        dto.setCustomerId(customer.getCustomerId());
        dto.setCustomerNumber(customer.getCustomerNumber());
        dto.setFirstName(customer.getFirstName());
        dto.setMiddleName(customer.getMiddleName());
        dto.setLastName(customer.getLastName());
        dto.setDateOfBirth(customer.getDateOfBirth());
        dto.setGender(customer.getGender());
        dto.setEmail(customer.getEmail());
        dto.setMobileNumber(customer.getMobileNumber());
        dto.setStatus(customer.getStatus());
        dto.setCustomerType(customer.getCustomerType());
        dto.setCreatedAt(customer.getCreatedAt());
        dto.setUpdatedAt(customer.getUpdatedAt());
        return dto;
    }
}