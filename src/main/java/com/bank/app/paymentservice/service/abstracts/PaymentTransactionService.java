package com.bank.app.paymentservice.service.abstracts;

import com.bank.app.paymentservice.models.dto.PaymentTransactionRequestDto;
import com.bank.app.paymentservice.models.dto.TransactionExpenseLimitResponse;

import java.util.List;

public interface PaymentTransactionService {
    void persistTransaction(PaymentTransactionRequestDto paymentTransactionDto);

    List<TransactionExpenseLimitResponse> getAllTransactionsByAccount(String account);

    List<TransactionExpenseLimitResponse> getAllTransactionsByAccountAndLimitExceeded(String account, boolean limitExceeded);
}
