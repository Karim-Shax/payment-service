package com.bank.app.paymentservice.service.impl;

import com.bank.app.paymentservice.models.converter.ExpenseLimitMapper;
import com.bank.app.paymentservice.models.dto.ExpenseLimitDto;
import com.bank.app.paymentservice.models.enums.ExpenseCategory;
import com.bank.app.paymentservice.repository.ExpenseLimitRepository;
import com.bank.app.paymentservice.service.abstracts.ExpenseLimitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Service
public class ExpenseLimitServiceImpl implements ExpenseLimitService {

    private final ExpenseLimitRepository expenseLimitRepository;
    private final ExpenseLimitMapper expenseLimitMapper;

    public ExpenseLimitServiceImpl(ExpenseLimitRepository expenseLimitRepository, ExpenseLimitMapper expenseLimitMapper) {
        this.expenseLimitRepository = expenseLimitRepository;
        this.expenseLimitMapper = expenseLimitMapper;
    }

    @Override
    public boolean isExistByAccountAndCategory(String account, ExpenseCategory expenseCategory) {
        return expenseLimitRepository.isExistByAccountAndCategory(account, expenseCategory);
    }

    @Override
    @Transactional
    public void setLimitSumByAccountAndCategory(BigDecimal sum, String account, ExpenseCategory expenseCategory) {
        expenseLimitRepository.setLimitSumByAccountAndCategory(sum, account, expenseCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseLimitDto> findAllByAccount(String account) {
        return expenseLimitRepository.findAllByAccount(account)
                .stream()
                .map(expenseLimitMapper::toDto)
                .toList();
    }
}
