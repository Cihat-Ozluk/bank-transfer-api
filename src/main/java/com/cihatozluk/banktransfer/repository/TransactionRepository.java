package com.cihatozluk.banktransfer.repository;

import com.cihatozluk.banktransfer.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}
