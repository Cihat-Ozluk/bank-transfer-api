package com.cihatozluk.banktransfer.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type",nullable = false)
    private TransactionType type;

    @Column(name = "amount",nullable = false,precision = 19,scale = 2)
    private BigDecimal amount;

    @ManyToOne(fetch=FetchType.LAZY,optional = true)
    @JoinColumn(name = "from_account_id",nullable = true)
    private Account fromAccount;

    @ManyToOne(fetch=FetchType.LAZY,optional = true)
    @JoinColumn(name = "to_account_id",nullable = true)
    private Account toAccount;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    protected Transaction(){

    }

    private Transaction(TransactionType type, BigDecimal amount, Account fromAccount, Account toAccount) {
        this.type = type;
        this.amount = amount;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    public static Transaction deposit(Account to, BigDecimal amount) {
        Objects.requireNonNull(to, "to account is required");
        return new Transaction(TransactionType.DEPOSIT, amount, null, to);
    }

    public static Transaction withdraw(Account from, BigDecimal amount) {
        Objects.requireNonNull(from, "from account is required");
        return new Transaction(TransactionType.WITHDRAW, amount, from, null);
    }

    public static Transaction transfer(Account from,  Account to, BigDecimal amount) {
        Objects.requireNonNull(from, "from account is required");
        Objects.requireNonNull(to, "to account is required");

        if (from.getId().equals(to.getId())) {
            throw new IllegalArgumentException("SAME_ACCOUNT");
        }

        return new Transaction(TransactionType.TRANSFER, amount, from, to);
    }

    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type=" + type +
                ", amount=" + amount +
                ", createdAt=" + createdAt +
                '}';
    }
}
