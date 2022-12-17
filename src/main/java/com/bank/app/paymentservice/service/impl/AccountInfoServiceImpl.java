package com.bank.app.paymentservice.service.impl;

import com.bank.app.paymentservice.models.enums.ExpenseCategory;
import com.bank.app.paymentservice.repository.AccountInfoRepository;
import com.bank.app.paymentservice.service.abstracts.AccountInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AccountInfoServiceImpl implements AccountInfoService {

    private final AccountInfoRepository accountInfoRepository;

    public AccountInfoServiceImpl(AccountInfoRepository accountInfoRepository) {
        this.accountInfoRepository = accountInfoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExistByAccount(String account) {
        return accountInfoRepository.isExistByAccount(account);
    }

}
