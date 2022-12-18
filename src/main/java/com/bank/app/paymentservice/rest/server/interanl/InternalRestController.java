package com.bank.app.paymentservice.rest.server.interanl;


import com.bank.app.paymentservice.exeptions.NotFoundException;
import com.bank.app.paymentservice.models.dto.PaymentTransactionRequestDto;
import com.bank.app.paymentservice.service.abstracts.AccountInfoService;
import com.bank.app.paymentservice.service.abstracts.PaymentTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/internal")
@Tag(
        name = "API для внутренних сервисов"
)
public class InternalRestController {
    private PaymentTransactionService paymentTransactionService;
    private AccountInfoService accountInfoService;

    public InternalRestController(PaymentTransactionService paymentTransactionService, AccountInfoService accountInfoService) {
        this.paymentTransactionService = paymentTransactionService;
        this.accountInfoService = accountInfoService;
    }

    @PostMapping("/transaction")
    @Operation(summary = "Сохранение транзакции и обработка изменения промежуточного лимита")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
            @ExampleObject(
                    name = "Транзакция",
                    description = "Информация о транзакции",
                    value = """
                            {
                                "accountFrom": 87770981997,
                                "accountTo": 1234567898,
                                "currencyShortName": "KZT",
                                "sum": 25000.00,
                                "expenseCategory": "SERVICE",
                                "dateTime" : "2022-12-15 18:00:24+06"
                            }
                            """)
    }))
    public ResponseEntity<Void> persistPaymentTransaction(@Valid @RequestBody() PaymentTransactionRequestDto paymentTransactionDto) {
        if (!accountInfoService.isExistByAccount(paymentTransactionDto.accountFrom())) {
            throw new NotFoundException("Аккаунт по номеру " + paymentTransactionDto.accountFrom() + " не найден");
        }
        paymentTransactionService.persistTransaction(paymentTransactionDto);
        return ResponseEntity.ok().build();
    }

}
