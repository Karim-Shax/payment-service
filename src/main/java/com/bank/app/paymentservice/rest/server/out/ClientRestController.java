package com.bank.app.paymentservice.rest.server.out;


import com.bank.app.paymentservice.exeptions.NotFoundException;
import com.bank.app.paymentservice.models.dto.ExpenseLimitDto;
import com.bank.app.paymentservice.models.dto.Response;
import com.bank.app.paymentservice.models.dto.TransactionExpenseLimitResponse;
import com.bank.app.paymentservice.models.enums.ExpenseCategory;
import com.bank.app.paymentservice.service.abstracts.AccountInfoService;
import com.bank.app.paymentservice.service.abstracts.ExpenseLimitService;
import com.bank.app.paymentservice.service.abstracts.PaymentTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/client")
@Tag(
        name = "API для клиента",
        description = "Все методы доступные клиенту, для операции с транзакциями и лимитами"
)
public class ClientRestController {

    private PaymentTransactionService paymentTransactionService;
    private ExpenseLimitService expenseLimitService;
    private AccountInfoService accountInfoService;

    public ClientRestController(PaymentTransactionService paymentTransactionService, ExpenseLimitService expenseLimitService, AccountInfoService accountInfoService) {
        this.paymentTransactionService = paymentTransactionService;
        this.expenseLimitService = expenseLimitService;
        this.accountInfoService = accountInfoService;
    }

    @GetMapping("/transaction/all")
    @Operation(summary = "Получить все транзакции по номеру счета")
    public ResponseEntity<List<TransactionExpenseLimitResponse>> getAllPaymentsByAccount(@RequestHeader String account) {
        if (!accountInfoService.isExistByAccount(account)) {
            throw new NotFoundException("Аккаунт по номеру " + account + " не найден");
        }
        return ResponseEntity.ok(paymentTransactionService.getAllTransactionsByAccount(account));
    }

    @GetMapping("/transaction/{limitExceeded}")
    @Operation(summary = "Получить все транзакции по номеру счета и флагу превышения лимита")
    public ResponseEntity<List<TransactionExpenseLimitResponse>> getPaymentsByAccountAndLimitExceeded(@RequestHeader String account,
                                                                                                      @PathVariable Boolean limitExceeded) {
        if (!accountInfoService.isExistByAccount(account)) {
            throw new NotFoundException("Аккаунт по номеру " + account + " не найден");
        }
        return ResponseEntity.ok(paymentTransactionService.getAllTransactionsByAccountAndLimitExceeded(account, limitExceeded));
    }

    @PutMapping("/limit")
    @Operation(summary = "Установить лимит для аккаунта по его номеру и категории")
    public ResponseEntity<Response> setLimitSumByAccountAndCategory(
            @RequestParam(value = "sum") BigDecimal sum,
            @RequestParam(value = "category") ExpenseCategory expenseCategory,
            @RequestHeader String account
    ) {
        if (!expenseLimitService.isExistByAccountAndCategory(account, expenseCategory)) {
            throw new NotFoundException("Лимит по номеру " + account + " и категории " + expenseCategory.name() + " не найден");
        }
        expenseLimitService.setLimitSumByAccountAndCategory(sum, account, expenseCategory);
        return ResponseEntity.ok(new Response(HttpStatus.OK.value(), "Лимит успешно изменён"));
    }

    @GetMapping("/limit/all")
    @Operation(summary = "Получить все лимиты")
    public ResponseEntity<List<ExpenseLimitDto>> getAllLimitsByAccount(@RequestHeader String account) {
        if (!accountInfoService.isExistByAccount(account)) {
            throw new NotFoundException("Аккаунт по номеру " + account + " не найден");
        }
        return ResponseEntity.ok(expenseLimitService.findAllByAccount(account));
    }
}
