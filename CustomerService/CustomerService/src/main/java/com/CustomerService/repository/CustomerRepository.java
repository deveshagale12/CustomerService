package com.CustomerService.repository;

import com.CustomerService.entity.Customer;
import com.CustomerService.entity.CustomerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByMobileNumber(String mobileNumber);

    Optional<Customer> findByCustomerNumber(String customerNumber);

    boolean existsByEmail(String email);

    boolean existsByMobileNumber(String mobileNumber);

    List<Customer> findByStatus(CustomerStatus status);

    @Modifying
    @Query("UPDATE Customer c SET c.status = :status WHERE c.customerId = :customerId")
    int updateStatus(@Param("customerId") Long customerId, @Param("status") CustomerStatus status);
}
