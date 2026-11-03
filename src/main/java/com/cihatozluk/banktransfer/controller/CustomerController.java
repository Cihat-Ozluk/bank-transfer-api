package com.cihatozluk.banktransfer.controller;

import com.cihatozluk.banktransfer.dto.CreateCustomerRequest;
import com.cihatozluk.banktransfer.dto.CustomerResponse;
import com.cihatozluk.banktransfer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@Valid @RequestBody CreateCustomerRequest customerRequest){
       return customerService.create(customerRequest);
    }

    @GetMapping("/{customerId}")
    public CustomerResponse getCustomer(@PathVariable Long customerId){
        return customerService.getById(customerId);
    }

}
