package com.cihatozluk.banktransfer.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="account_number",nullable = false,unique = true)
    private String accountNumber;

    @Column(name="balance",nullable = false,precision = 19,scale = 2)
    private BigDecimal balance=BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency",nullable = false)
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "customer_id",nullable = false)
    private Customer customer;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false,nullable = false)
    private LocalDateTime createdAt;

    public Account(){}

    public Account(String accountNumber, Currency currency, Customer customer) {
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.customer = customer;
    }

    public void deposit(BigDecimal amount){

        validateAmount(amount);
        balance=balance.add(amount);
    }

    public void withdraw(BigDecimal amount){

        validateAmount(amount);

        if(amount.compareTo(balance)>0){
            throw new IllegalArgumentException("INSUFFICIENT_BALANCE");
        }

        balance=balance.subtract(amount);
    }

    private void validateAmount(BigDecimal amount){

        if(amount==null){
            throw new IllegalArgumentException("AMOUNT_REQUIRED");
        }

        if(amount.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("INVALID_AMOUNT");
        }
    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", currency=" + currency +
                ", createdAt=" + createdAt +
                '}';
    }
}
