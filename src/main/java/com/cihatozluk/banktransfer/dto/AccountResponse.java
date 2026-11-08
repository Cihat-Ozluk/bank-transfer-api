package com.cihatozluk.banktransfer.dto;

import com.cihatozluk.banktransfer.entity.Account;
import com.cihatozluk.banktransfer.entity.Currency;

import java.math.BigDecimal;

public record AccountResponse(
        Long id,
        Long customerId,
        String accountNumber,
        BigDecimal balance,
        Currency currency
) {

    public static AccountResponse from(Account account){
        return new AccountResponse(
                account.getId(),
                account.getCustomer().getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getCurrency()
        );
    }
}
