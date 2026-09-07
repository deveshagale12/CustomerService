package com.CustomerService.repository;

import com.CustomerService.entity.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;



public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {

    List<CustomerAddress> findByCustomerId(Long customerId);

    Optional<CustomerAddress> findByCustomerIdAndIsPrimaryTrue(Long customerId);

    void deleteByCustomerId(Long customerId);
}
