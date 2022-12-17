package com.bank.app.paymentservice.models.dto;

import com.bank.app.paymentservice.models.enums.CurrencyShortName;
import com.bank.app.paymentservice.models.enums.ExpenseCategory;
import com.bank.app.paymentservice.util.DateTimeFormatter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class TransactionExpenseLimitResponse {
    private String accountFrom;
    private String accountTo;
    private CurrencyShortName currencyShortName;
    private BigDecimal sum;
    private ExpenseCategory expenseCategory;
    private String dateTime;
    private BigDecimal limitSum;
    private String limitDateTime;
    private CurrencyShortName limitCurrencyShortName;

    public TransactionExpenseLimitResponse() {
    }


    public TransactionExpenseLimitResponse(String accountFrom,
                                           String accountTo,
                                           CurrencyShortName currencyShortName,
                                           BigDecimal sum,
                                           ExpenseCategory expenseCategory,
                                           ZonedDateTime dateTime,
                                           BigDecimal limitSum,
                                           ZonedDateTime limitDateTime,
                                           CurrencyShortName limitCurrencyShortName) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.currencyShortName = currencyShortName;
        this.sum = sum;
        this.expenseCategory = expenseCategory;
        this.dateTime = dateTime.format(DateTimeFormatter.format());
        this.limitSum = limitSum;
        this.limitDateTime = limitDateTime.format(DateTimeFormatter.format());
        this.limitCurrencyShortName = limitCurrencyShortName;
    }

    public String getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(String accountFrom) {
        this.accountFrom = accountFrom;
    }

    public String getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(String accountTo) {
        this.accountTo = accountTo;
    }

    public CurrencyShortName getCurrencyShortName() {
        return currencyShortName;
    }

    public void setCurrencyShortName(CurrencyShortName currencyShortName) {
        this.currencyShortName = currencyShortName;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public ExpenseCategory getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(ExpenseCategory expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public BigDecimal getLimitSum() {
        return limitSum;
    }

    public void setLimitSum(BigDecimal limitSum) {
        this.limitSum = limitSum;
    }

    public String getLimitDateTime() {
        return limitDateTime;
    }

    public void setLimitDateTime(String limitDateTime) {
        this.limitDateTime = limitDateTime;
    }

    public CurrencyShortName getLimitCurrencyShortName() {
        return limitCurrencyShortName;
    }

    public void setLimitCurrencyShortName(CurrencyShortName limitCurrencyShortName) {
        this.limitCurrencyShortName = limitCurrencyShortName;
    }
}
