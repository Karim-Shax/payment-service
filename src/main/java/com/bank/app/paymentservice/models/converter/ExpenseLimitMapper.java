package com.bank.app.paymentservice.models.converter;

import com.bank.app.paymentservice.models.dto.ExpenseLimitDto;
import com.bank.app.paymentservice.models.entities.ExpenseLimit;
import com.bank.app.paymentservice.util.DateTimeFormatter;
import org.springframework.stereotype.Service;

@Service
public class ExpenseLimitMapper {

    public ExpenseLimitDto toDto(ExpenseLimit expenseLimit) {
        return new ExpenseLimitDto(
                expenseLimit.getCurrency(),
                expenseLimit.getLimitDateTime().format(DateTimeFormatter.format()),
                expenseLimit.getLimitSum(),
                expenseLimit.getExpenseCategory()
        );
    }
}
