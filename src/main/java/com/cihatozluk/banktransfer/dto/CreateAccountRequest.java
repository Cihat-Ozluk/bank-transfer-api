package com.cihatozluk.banktransfer.dto;

import com.cihatozluk.banktransfer.entity.Currency;
import jakarta.validation.constraints.NotNull;

public record CreateAccountRequest(
        @NotNull(message="Customer id required")
        Long customerId,
        @NotNull(message="Currency type required")
        Currency currency
) {
}
