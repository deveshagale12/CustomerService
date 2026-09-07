package com.CustomerService.service;

import com.CustomerService.dto.CustomerAddressDto;
import com.CustomerService.entity.CustomerAddress;
import com.CustomerService.exception.AddressNotFoundException;
import com.CustomerService.exception.CustomerNotFoundException;
import com.CustomerService.repository.CustomerAddressRepository;
import com.CustomerService.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerAddressService {

    private final CustomerAddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    public CustomerAddressService(CustomerAddressRepository addressRepository, CustomerRepository customerRepository) {
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerAddressDto addAddress(Long customerId, CustomerAddressDto dto) {
        ensureCustomerExists(customerId);

        CustomerAddress address = new CustomerAddress();
        address.setCustomerId(customerId);
        address.setAddressType(dto.getAddressType());
        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setCountry(dto.getCountry());
        address.setPostalCode(dto.getPostalCode());

        boolean noExistingAddresses = addressRepository.findByCustomerId(customerId).isEmpty();
        if (dto.isPrimary() || noExistingAddresses) {
            clearExistingPrimary(customerId);
            address.setPrimary(true);
        }

        CustomerAddress saved = addressRepository.save(address);
        return toDto(saved);
    }

    public List<CustomerAddressDto> getCustomerAddresses(Long customerId) {
        ensureCustomerExists(customerId);
        return addressRepository.findByCustomerId(customerId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CustomerAddressDto getPrimaryAddress(Long customerId) {
        ensureCustomerExists(customerId);
        CustomerAddress address = addressRepository.findByCustomerIdAndIsPrimaryTrue(customerId)
                .orElseThrow(() -> new AddressNotFoundException("No primary address found for customer id: " + customerId));
        return toDto(address);
    }

    @Transactional
    public CustomerAddressDto updateAddress(Long customerId, Long addressId, CustomerAddressDto dto) {
        CustomerAddress address = findAddressOrThrow(customerId, addressId);

        address.setAddressType(dto.getAddressType());
        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setCountry(dto.getCountry());
        address.setPostalCode(dto.getPostalCode());

        CustomerAddress saved = addressRepository.save(address);
        return toDto(saved);
    }

    @Transactional
    public void deleteAddress(Long customerId, Long addressId) {
        CustomerAddress address = findAddressOrThrow(customerId, addressId);
        addressRepository.delete(address);
    }

    @Transactional
    public CustomerAddressDto setPrimaryAddress(Long customerId, Long addressId) {
        CustomerAddress address = findAddressOrThrow(customerId, addressId);
        clearExistingPrimary(customerId);
        address.setPrimary(true);
        CustomerAddress saved = addressRepository.save(address);
        return toDto(saved);
    }

    private void clearExistingPrimary(Long customerId) {
        addressRepository.findByCustomerIdAndIsPrimaryTrue(customerId).ifPresent(existing -> {
            existing.setPrimary(false);
            addressRepository.save(existing);
        });
    }

    private CustomerAddress findAddressOrThrow(Long customerId, Long addressId) {
        CustomerAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + addressId));
        if (!address.getCustomerId().equals(customerId)) {
            throw new AddressNotFoundException("Address " + addressId + " does not belong to customer " + customerId);
        }
        return address;
    }

    private void ensureCustomerExists(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException("Customer not found with id: " + customerId);
        }
    }

    private CustomerAddressDto toDto(CustomerAddress address) {
        CustomerAddressDto dto = new CustomerAddressDto();
        dto.setAddressId(address.getAddressId());
        dto.setAddressType(address.getAddressType());
        dto.setAddressLine1(address.getAddressLine1());
        dto.setAddressLine2(address.getAddressLine2());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setCountry(address.getCountry());
        dto.setPostalCode(address.getPostalCode());
        dto.setPrimary(address.isPrimary());
        return dto;
    }
}
