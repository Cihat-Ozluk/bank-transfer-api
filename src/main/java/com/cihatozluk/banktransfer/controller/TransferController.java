package com.cihatozluk.banktransfer.controller;

import com.cihatozluk.banktransfer.dto.TransactionResponse;
import com.cihatozluk.banktransfer.dto.TransferRequest;
import com.cihatozluk.banktransfer.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final AccountService accountService;

    public TransferController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse transfer(@Valid @RequestBody TransferRequest request) {
        return accountService.transfer(request);
    }
}
