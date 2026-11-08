package com.cihatozluk.banktransfer.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AmountRequest(
        @NotNull(message="Amount is required")
        @Positive(message="Amount must be positive")
        @Digits(integer = 17,fraction = 2,
                message = "Amount can have at most 17 integer digits and 2 decimal places")
        BigDecimal amount
) {
}
