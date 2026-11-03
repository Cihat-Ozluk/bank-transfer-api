package com.cihatozluk.banktransfer.exception;

import com.cihatozluk.banktransfer.dto.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCustomerNotFound(CustomerNotFoundException exc){
        return ErrorResponse.of("CUSTOMER_NOT_FOUND",exc.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailAlreadyExists(EmailAlreadyExistsException exc){
        return ErrorResponse.of("EMAIL_ALREADY_EXISTS",exc.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolation(DataIntegrityViolationException exc){
        return ErrorResponse.of("DATA_INTEGRITY_VIOLATION",
                "Request conflicts with existing data");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException exc){

        Map<String,String> fieldErrors=new HashMap<>();

        for(FieldError error:exc.getBindingResult().getFieldErrors()){
            fieldErrors.putIfAbsent(error.getField(), error.getDefaultMessage());
        }

        return ErrorResponse.of("VALIDATION_FAILED","Request validation failed",fieldErrors);
    }
}
