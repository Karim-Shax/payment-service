package com.bank.app.paymentservice.models.converter;

import com.bank.app.paymentservice.models.dto.PaymentTransactionRequestDto;
import com.bank.app.paymentservice.models.entities.AccountInfo;
import com.bank.app.paymentservice.models.entities.PaymentTransaction;
import com.bank.app.paymentservice.util.DateTimeFormatter;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class PaymentTransactionMapper {

    public PaymentTransaction toEntity(PaymentTransactionRequestDto paymentTransactionDto, AccountInfo accountInfo) {
        PaymentTransaction paymentTransaction = new PaymentTransaction();
        paymentTransaction.setAccountFrom(accountInfo);
        paymentTransaction.setAccountTo(paymentTransactionDto.accountTo());
        paymentTransaction.setTransactionSum(paymentTransactionDto.sum());
        paymentTransaction.setCurrencyShortName(paymentTransactionDto.currencyShortName());
        paymentTransaction.setDateTime(ZonedDateTime.parse(paymentTransactionDto.dateTime(),
                DateTimeFormatter.format()));
        paymentTransaction.setExpenseCategory(paymentTransactionDto.expenseCategory());
        return paymentTransaction;
    }
}
