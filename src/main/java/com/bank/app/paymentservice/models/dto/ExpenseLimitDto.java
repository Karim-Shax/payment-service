package com.bank.app.paymentservice.models.dto;

import com.bank.app.paymentservice.models.enums.CurrencyShortName;
import com.bank.app.paymentservice.models.enums.ExpenseCategory;

import java.math.BigDecimal;

public record ExpenseLimitDto (
        CurrencyShortName currency,
        String limitDateTime,
        BigDecimal limitSum,
        ExpenseCategory expenseCategory){
}
