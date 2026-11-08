package com.cihatozluk.banktransfer.service;

import com.cihatozluk.banktransfer.dto.*;
import com.cihatozluk.banktransfer.entity.Account;
import com.cihatozluk.banktransfer.entity.Customer;
import com.cihatozluk.banktransfer.entity.Transaction;
import com.cihatozluk.banktransfer.exception.AccountNotFoundException;
import com.cihatozluk.banktransfer.exception.CurrencyMismatchException;
import com.cihatozluk.banktransfer.exception.CustomerNotFoundException;
import com.cihatozluk.banktransfer.exception.SameAccountTransferException;
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

    private Account findForUpdate(String accountNumber) {
        Account account = accountRepository.findByAccountNumberForUpdate(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        return account;
    }

    @Transactional
    public AccountResponse deposit(String accountNumber, AmountRequest request) {
        Account account = findForUpdate(accountNumber);

        account.deposit(request.amount());

        Transaction transaction = Transaction.deposit(account, request.amount());
        transactionRepository.save(transaction);

        return AccountResponse.from(account);
    }

    @Transactional
    public AccountResponse withdraw(String accountNumber, AmountRequest request) {
        Account account = findForUpdate(accountNumber);

        account.withdraw(request.amount());

        Transaction transaction = Transaction.withdraw(account, request.amount());
        transactionRepository.save(transaction);

        return AccountResponse.from(account);
    }

    @Transactional
    public TransactionResponse transfer(TransferRequest request) {
        if (request.fromAccountNumber().equals(request.toAccountNumber())) {
            throw new SameAccountTransferException();
        }

        boolean fromFirst = request.fromAccountNumber().compareTo(request.toAccountNumber()) < 0;

        Account fromAccount;
        Account toAccount;

        if (fromFirst) {
            fromAccount = findForUpdate(request.fromAccountNumber());
            toAccount = findForUpdate(request.toAccountNumber());
        } else {
            toAccount = findForUpdate(request.toAccountNumber());
            fromAccount = findForUpdate(request.fromAccountNumber());
        }

        if (fromAccount.getCurrency() != toAccount.getCurrency()) {
            throw new CurrencyMismatchException();
        }

        Transaction transaction = Transaction.transfer(
                fromAccount,
                toAccount,
                request.amount()
        );

        fromAccount.withdraw(request.amount());
        toAccount.deposit(request.amount());

        Transaction savedTransaction = transactionRepository.save(transaction);

        return TransactionResponse.from(savedTransaction);

    }
}
