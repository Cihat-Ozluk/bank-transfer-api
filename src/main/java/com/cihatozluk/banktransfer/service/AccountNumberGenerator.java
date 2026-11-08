package com.cihatozluk.banktransfer.service;

import com.cihatozluk.banktransfer.repository.AccountRepository;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class AccountNumberGenerator {

    private static final int MAX_ATTEMPTS = 5;
    private final AccountRepository accountRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public AccountNumberGenerator(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public String generate() {
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            String number = String.valueOf(secureRandom.nextLong(1_000_000_000L, 10_000_000_000L));
            if (!accountRepository.existsByAccountNumber(number)) {
                return number;
            }
        }
        throw new IllegalStateException("Could not generate a unique account number");
    }
}
