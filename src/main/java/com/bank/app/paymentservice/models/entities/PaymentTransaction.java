package com.bank.app.paymentservice.models.entities;


import com.bank.app.paymentservice.models.enums.CurrencyShortName;
import com.bank.app.paymentservice.models.enums.ExpenseCategory;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "payment_transactions")
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "account_info_id")
    private AccountInfo accountFrom;

    @Column(name = "account_to", columnDefinition = "varchar(20) not null")
    private String accountTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_short_name", columnDefinition = "varchar(10) not null")
    private CurrencyShortName currencyShortName;

    @Column(name = "transaction_sum", nullable = false)
    private BigDecimal transactionSum;

    @Type(type = "org.hibernate.type.ZonedDateTimeType")
    @Column(name = "date_time")
    private ZonedDateTime dateTime;

    @Column(name = "limit_exceeded", columnDefinition = "int2")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean limitExceeded;

    @Enumerated(EnumType.STRING)
    @Column(name = "expense_category", columnDefinition = "varchar(20) not null")
    private ExpenseCategory expenseCategory;

    public PaymentTransaction(AccountInfo accountFrom, String accountTo, CurrencyShortName currencyShortName, BigDecimal transactionSum, ZonedDateTime dateTime, Boolean limitExceeded, ExpenseCategory expenseCategory) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.currencyShortName = currencyShortName;
        this.transactionSum = transactionSum;
        this.dateTime = dateTime;
        this.limitExceeded = limitExceeded;
        this.expenseCategory = expenseCategory;
    }

    public PaymentTransaction() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountInfo getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(AccountInfo accountFrom) {
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

    public BigDecimal getTransactionSum() {
        return transactionSum;
    }

    public void setTransactionSum(BigDecimal transactionSum) {
        this.transactionSum = transactionSum;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Boolean getLimitExceeded() {
        return limitExceeded;
    }

    public void setLimitExceeded(Boolean limitExceeded) {
        this.limitExceeded = limitExceeded;
    }

    public ExpenseCategory getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(ExpenseCategory expenseCategory) {
        this.expenseCategory = expenseCategory;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentTransaction that = (PaymentTransaction) o;

        if (!Objects.equals(accountFrom.getAccount(), that.accountFrom.getAccount())) return false;
        if (!Objects.equals(accountTo, that.accountTo)) return false;
        if (currencyShortName != that.currencyShortName) return false;
        if (!Objects.equals(transactionSum, that.transactionSum))
            return false;
        if (!Objects.equals(dateTime, that.dateTime)) return false;
        if (!Objects.equals(limitExceeded, that.limitExceeded))
            return false;
        return expenseCategory == that.expenseCategory;
    }

    @Override
    public int hashCode() {
        int result = accountFrom.getAccount() != null ? accountFrom.getAccount().hashCode() : 0;
        result = 31 * result + (accountTo != null ? accountTo.hashCode() : 0);
        result = 31 * result + (currencyShortName != null ? currencyShortName.hashCode() : 0);
        result = 31 * result + (transactionSum != null ? transactionSum.hashCode() : 0);
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = 31 * result + (limitExceeded != null ? limitExceeded.hashCode() : 0);
        result = 31 * result + (expenseCategory != null ? expenseCategory.hashCode() : 0);
        return result;
    }

}
