package com.bank.app.paymentservice.rest.server.interanl;


import com.bank.app.paymentservice.exeptions.NotFoundException;
import com.bank.app.paymentservice.models.dto.PaymentTransactionRequestDto;
import com.bank.app.paymentservice.service.abstracts.AccountInfoService;
import com.bank.app.paymentservice.service.abstracts.PaymentTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/internal")
public class InternalRestController {
    private PaymentTransactionService paymentTransactionService;
    private AccountInfoService accountInfoService;

    public InternalRestController(PaymentTransactionService paymentTransactionService, AccountInfoService accountInfoService) {
        this.paymentTransactionService = paymentTransactionService;
        this.accountInfoService = accountInfoService;
    }

    @PostMapping("/transaction")
    public ResponseEntity<Void> persistPaymentTransaction(@Valid @RequestBody PaymentTransactionRequestDto paymentTransactionDto) {
        if (!accountInfoService.isExistByAccount(paymentTransactionDto.accountFrom())) {
            throw new NotFoundException("Аккаунт по номеру " + paymentTransactionDto.accountFrom() + " не найден");
        }
        paymentTransactionService.persistTransaction(paymentTransactionDto);
        return ResponseEntity.ok().build();
    }

}
