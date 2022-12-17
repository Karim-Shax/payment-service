package com.bank.app.paymentservice.service.impl;

import com.bank.app.paymentservice.exeptions.LimitExceededException;
import com.bank.app.paymentservice.models.converter.ExpenseLimitMapper;
import com.bank.app.paymentservice.models.converter.PaymentTransactionMapper;
import com.bank.app.paymentservice.models.dto.PaymentTransactionRequestDto;
import com.bank.app.paymentservice.models.dto.TransactionExpenseLimitResponse;
import com.bank.app.paymentservice.models.entities.AccountInfo;
import com.bank.app.paymentservice.models.entities.ExpenseLimit;
import com.bank.app.paymentservice.models.entities.PaymentTransaction;
import com.bank.app.paymentservice.models.enums.CurrencyShortName;
import com.bank.app.paymentservice.repository.AccountInfoRepository;
import com.bank.app.paymentservice.repository.ExpenseLimitRepository;
import com.bank.app.paymentservice.repository.PaymentTransactionRepository;
import com.bank.app.paymentservice.service.CurrencyExchangerService;
import com.bank.app.paymentservice.service.abstracts.PaymentTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentTransactionServiceImpl implements PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final AccountInfoRepository accountInfoRepository;
    private final PaymentTransactionMapper paymentTransactionMapper;
    private final CurrencyExchangerService exchangerService;
    private final ExpenseLimitRepository expenseLimitRepository;


    @Override
    @Transactional(readOnly = true)
    public List<TransactionExpenseLimitResponse> getAllTransactionsByAccount(String account) {
        return paymentTransactionRepository.getAllTransactionsByAccount(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionExpenseLimitResponse> getAllTransactionsByAccountAndLimitExceeded(String account, boolean limitExceeded) {
        return paymentTransactionRepository.getAllByAccountAndLimitExceeded(account, limitExceeded);
    }

    @Override
    @Transactional
    public void persistTransaction(PaymentTransactionRequestDto paymentTransactionDto) {
        Optional<ExpenseLimit> limitOptional = expenseLimitRepository.finByAccountAndCurrencyAndCategory(
                paymentTransactionDto.accountFrom(),
                CurrencyShortName.USD,
                paymentTransactionDto.expenseCategory());
        AccountInfo accountInfo = accountInfoRepository.findByAccount(paymentTransactionDto.accountFrom());
        PaymentTransaction paymentTransaction = paymentTransactionMapper.toEntity(paymentTransactionDto, accountInfo);
        ExpenseLimit expenseLimit;
        if (limitOptional.isPresent()) {
            expenseLimit = limitOptional.get();
            checkAndSetLimitExceeded(paymentTransaction, expenseLimit);
            expenseLimit.setAccountInfo(accountInfo);
            expenseLimitRepository.save(expenseLimit);
        } else {
            paymentTransaction.setLimitExceeded(false);
        }
        paymentTransactionRepository.save(paymentTransaction);
    }

    private void checkAndSetLimitExceeded(PaymentTransaction paymentTransaction, ExpenseLimit expenseLimit) {
        BigDecimal paymentSum = paymentTransaction.getTransactionSum().setScale(2, RoundingMode.HALF_UP);
        BigDecimal expenseLimitTmp = expenseLimit.getLimitTemporary();
        paymentSum = paymentTransaction
                .getCurrencyShortName() == CurrencyShortName.KZT ?
                exchangerService.getAmountKztToUsd(paymentSum) : exchangerService.getAmountRubToUsd(paymentSum);
        setLimitExceeded(paymentTransaction, expenseLimit, paymentSum, expenseLimitTmp);
    }

    private void setLimitExceeded(PaymentTransaction paymentTransaction, ExpenseLimit expenseLimit, BigDecimal paymentSum, BigDecimal expenseLimitTmp) {
        if (expenseLimitTmp.compareTo(BigDecimal.valueOf(0.00)) > 0) {
            BigDecimal resultSubstract = expenseLimitTmp.subtract(paymentSum);
            if (resultSubstract.compareTo(BigDecimal.valueOf(0.00)) >= 0) {
                expenseLimit.setLimitTemporary(resultSubstract);
                paymentTransaction.setLimitExceeded(false);
            } else {
                paymentTransaction.setLimitExceeded(true);
            }
        } else {
            paymentTransaction.setLimitExceeded(true);
        }
    }
}
