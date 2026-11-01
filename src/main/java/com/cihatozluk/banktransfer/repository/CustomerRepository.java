package com.cihatozluk.banktransfer.repository;

import com.cihatozluk.banktransfer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);
}
