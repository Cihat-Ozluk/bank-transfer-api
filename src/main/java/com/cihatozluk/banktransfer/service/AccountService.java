package com.cihatozluk.banktransfer.service;

import com.cihatozluk.banktransfer.dto.AccountResponse;
import com.cihatozluk.banktransfer.dto.AmountRequest;
import com.cihatozluk.banktransfer.dto.CreateAccountRequest;
import com.cihatozluk.banktransfer.entity.Account;
import com.cihatozluk.banktransfer.entity.Customer;
import com.cihatozluk.banktransfer.entity.Transaction;
import com.cihatozluk.banktransfer.exception.AccountNotFoundException;
import com.cihatozluk.banktransfer.exception.CustomerNotFoundException;
import com.cihatozluk.banktransfer.repository.AccountRepository;
import com.cihatozluk.banktransfer.repository.CustomerRepository;
import com.cihatozluk.banktransfer.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final TransactionRepository transactionRepository;

    @Autowired
    public AccountService(
            AccountRepository accountRepository,
            CustomerRepository customerRepository,
            AccountNumberGenerator accountNumberGenerator,
            TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.accountNumberGenerator = accountNumberGenerator;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public AccountResponse create(CreateAccountRequest accountRequest) {

        Customer customer = customerRepository.findById(accountRequest.customerId())
                .orElseThrow(() -> new CustomerNotFoundException(accountRequest.customerId()));

        String accountNumber = accountNumberGenerator.generate();

        Account account = new Account(
                accountNumber,
                accountRequest.currency(),
                customer
        );

        Account savedAccount = accountRepository.save(account);

        return AccountResponse.from(savedAccount);
    }

    @Transactional(readOnly = true)
    public AccountResponse getByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        return AccountResponse.from(account);
    }

    @Transactional
    public AccountResponse deposit(String accountNumber, AmountRequest request) {
        Account account = accountRepository.findByAccountNumberForUpdate(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        account.deposit(request.amount());

        Transaction transaction = Transaction.deposit(account, request.amount());
        transactionRepository.save(transaction);

        return AccountResponse.from(account);
    }

    @Transactional
    public AccountResponse withdraw(String accountNumber, AmountRequest request) {
        Account account = accountRepository.findByAccountNumberForUpdate(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        account.withdraw(request.amount());

        Transaction transaction = Transaction.withdraw(account, request.amount());
        transactionRepository.save(transaction);

        return AccountResponse.from(account);
    }
}
