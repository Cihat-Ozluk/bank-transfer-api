package com.cihatozluk.banktransfer.service;

import com.cihatozluk.banktransfer.dto.CreateCustomerRequest;
import com.cihatozluk.banktransfer.dto.CustomerResponse;
import com.cihatozluk.banktransfer.entity.Customer;
import com.cihatozluk.banktransfer.exception.CustomerNotFoundException;
import com.cihatozluk.banktransfer.exception.EmailAlreadyExistsException;
import com.cihatozluk.banktransfer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerResponse create(CreateCustomerRequest customerRequest){

        String email = customerRequest.email().trim().toLowerCase(Locale.ROOT);

        if(customerRepository.existsByEmail(email)){
            throw new EmailAlreadyExistsException(email);
        }

        Customer customer=new Customer(
                customerRequest.firstName(),
                customerRequest.lastName(),
                email
        );

        Customer dbCustomer=customerRepository.save(customer);

        return CustomerResponse.from(dbCustomer);
    }

    @Transactional(readOnly = true)
    public CustomerResponse getById(Long id){

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        return CustomerResponse.from(customer);
    }

}
