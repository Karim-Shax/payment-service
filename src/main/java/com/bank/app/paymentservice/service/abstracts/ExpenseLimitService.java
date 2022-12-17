package com.bank.app.paymentservice.service.abstracts;

import com.bank.app.paymentservice.models.dto.ExpenseLimitDto;
import com.bank.app.paymentservice.models.enums.ExpenseCategory;

import java.math.BigDecimal;
import java.util.List;


public interface ExpenseLimitService {

    boolean isExistByAccountAndCategory(String account, ExpenseCategory expenseCategory);

    void setLimitSumByAccountAndCategory(BigDecimal sum, String account, ExpenseCategory expenseCategory);

    List<ExpenseLimitDto> findAllByAccount(String account);

}
