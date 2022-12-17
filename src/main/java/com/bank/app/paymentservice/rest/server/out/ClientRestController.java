package com.bank.app.paymentservice.rest.server.out;


import com.bank.app.paymentservice.exeptions.NotFoundException;
import com.bank.app.paymentservice.models.dto.ExpenseLimitDto;
import com.bank.app.paymentservice.models.dto.Response;
import com.bank.app.paymentservice.models.dto.TransactionExpenseLimitResponse;
import com.bank.app.paymentservice.models.enums.ExpenseCategory;
import com.bank.app.paymentservice.service.abstracts.AccountInfoService;
import com.bank.app.paymentservice.service.abstracts.ExpenseLimitService;
import com.bank.app.paymentservice.service.abstracts.PaymentTransactionService;
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
    public ResponseEntity<List<TransactionExpenseLimitResponse>> getAllPaymentsByAccount(@RequestHeader String account) {
        if (!accountInfoService.isExistByAccount(account)) {
            throw new NotFoundException("Аккаунт по номеру " + account + " не найден");
        }
        return ResponseEntity.ok(paymentTransactionService.getAllTransactionsByAccount(account));
    }

    @GetMapping("/transaction/{limitExceeded}")
    public ResponseEntity<List<TransactionExpenseLimitResponse>> getPaymentsByAccountAndLimitExceeded(@RequestHeader String account,
                                                                                                      @PathVariable Boolean limitExceeded) {
        if (!accountInfoService.isExistByAccount(account)) {
            throw new NotFoundException("Аккаунт по номеру " + account + " не найден");
        }
        return ResponseEntity.ok(paymentTransactionService.getAllTransactionsByAccountAndLimitExceeded(account, limitExceeded));
    }

    @PutMapping("/limit")
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
    public ResponseEntity<List<ExpenseLimitDto>> getAllLimitsByAccount(@RequestHeader String account) {
        if (!accountInfoService.isExistByAccount(account)) {
            throw new NotFoundException("Аккаунт по номеру " + account + " не найден");
        }
        return ResponseEntity.ok(expenseLimitService.findAllByAccount(account));
    }
}
