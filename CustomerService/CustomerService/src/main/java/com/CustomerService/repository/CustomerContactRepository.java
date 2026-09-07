package com.CustomerService.repository;

import com.CustomerService.entity.CustomerContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CustomerContactRepository extends JpaRepository<CustomerContact, Long> {

    List<CustomerContact> findByCustomerId(Long customerId);

    List<CustomerContact> findByCustomerIdAndContactType(Long customerId, String contactType);
}
