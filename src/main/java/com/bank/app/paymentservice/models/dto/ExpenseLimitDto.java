package com.bank.app.paymentservice.models.dto;

import com.bank.app.paymentservice.models.enums.CurrencyShortName;
import com.bank.app.paymentservice.models.enums.ExpenseCategory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;


@Schema(description = "Информация о лимите пользователя")
public record ExpenseLimitDto (
        CurrencyShortName currency,
        String limitDateTime,
        BigDecimal limitSum,
        ExpenseCategory expenseCategory){
}
