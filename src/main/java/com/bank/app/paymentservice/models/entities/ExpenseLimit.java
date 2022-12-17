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
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "expense_limits", uniqueConstraints =
        {
                @UniqueConstraint(columnNames ={ "account_info_id", "expense_category"})
        })
public class ExpenseLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "limit_currency_shortname", columnDefinition = "varchar(10) default 'USD'")
    private CurrencyShortName currency;

    @Column(name = "limit_date_time", columnDefinition = "TIMESTAMP")
    @Type(type = "org.hibernate.type.ZonedDateTimeType")
    private ZonedDateTime limitDateTime;

    @Column(name = "limit_sum", columnDefinition = "numeric(19, 2) default 0.00")
    private BigDecimal limitSum;

    @Column(name = "limit_temporary", columnDefinition = "numeric(19, 2) default 0.00")
    private BigDecimal limitTemporary;

    @Enumerated(EnumType.STRING)
    @Column(name = "expense_category")
    private ExpenseCategory expenseCategory;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "account_info_id")
    private AccountInfo accountInfo;

    public ExpenseLimit(CurrencyShortName currency, ZonedDateTime limitDateTime, BigDecimal limitSum, BigDecimal limitTemporary, ExpenseCategory expenseCategory, AccountInfo accountInfo) {
        this.currency = currency;
        this.limitDateTime = limitDateTime;
        this.limitSum = limitSum;
        this.limitTemporary = limitTemporary;
        this.accountInfo = accountInfo;
        this.expenseCategory = expenseCategory;
    }

    public ExpenseLimit() {
    }


    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }


    public ExpenseCategory getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(ExpenseCategory expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CurrencyShortName getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyShortName currency) {
        this.currency = currency;
    }

    public ZonedDateTime getLimitDateTime() {
        return limitDateTime;
    }

    public void setLimitDateTime(ZonedDateTime limitDateTime) {
        this.limitDateTime = limitDateTime;
    }

    public BigDecimal getLimitSum() {
        return limitSum;
    }

    public void setLimitSum(BigDecimal limitSum) {
        this.limitSum = limitSum;
    }

    public BigDecimal getLimitTemporary() {
        return limitTemporary;
    }

    public void setLimitTemporary(BigDecimal limitTemporary) {
        this.limitTemporary = limitTemporary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpenseLimit that = (ExpenseLimit) o;

        if (currency != that.currency) return false;
        if (!Objects.equals(limitDateTime, that.limitDateTime))
            return false;
        if (!Objects.equals(limitSum, that.limitSum)) return false;
        if (!Objects.equals(limitTemporary, that.limitTemporary)) return false;
        if (expenseCategory != that.expenseCategory) return false;
        return Objects.equals(accountInfo.getAccount(), that.accountInfo.getAccount());
    }

    @Override
    public int hashCode() {
        int result = currency != null ? currency.hashCode() : 0;
        result = 31 * result + (limitDateTime != null ? limitDateTime.hashCode() : 0);
        result = 31 * result + (limitSum != null ? limitSum.hashCode() : 0);
        result = 31 * result + (limitTemporary != null ? limitTemporary.hashCode() : 0);
        result = 31 * result + (expenseCategory != null ? expenseCategory.hashCode() : 0);
        result = 31 * result + (accountInfo.getAccount() != null ? accountInfo.getAccount().hashCode() : 0);
        return result;
    }
}
