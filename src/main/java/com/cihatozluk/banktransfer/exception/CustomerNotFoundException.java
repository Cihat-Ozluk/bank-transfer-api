package com.cihatozluk.banktransfer.exception;

public class CustomerNotFoundException extends RuntimeException{
    public CustomerNotFoundException(Long id){
        super("Customer not found with id: " + id);
    }
}
