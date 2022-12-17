package com.bank.app.paymentservice.models.dto;

import com.bank.app.paymentservice.models.enums.CurrencyShortName;
import com.bank.app.paymentservice.models.enums.ExpenseCategory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;


public record PaymentTransactionRequestDto(
        @Size(min = 10) String accountFrom,
        @Size(min = 10) String accountTo,
        @NotNull() CurrencyShortName currencyShortName,
        @NotNull() BigDecimal sum,
        @NotNull() ExpenseCategory expenseCategory,
        @NotNull() String dateTime
) {
}
