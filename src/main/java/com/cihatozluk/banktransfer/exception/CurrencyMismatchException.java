package com.cihatozluk.banktransfer.exception;

public class CurrencyMismatchException extends RuntimeException {
    public CurrencyMismatchException() {
        super("Accounts must have the same currency");
    }
}
