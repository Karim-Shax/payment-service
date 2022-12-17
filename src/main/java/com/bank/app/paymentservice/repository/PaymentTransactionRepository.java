package com.bank.app.paymentservice.repository;

import com.bank.app.paymentservice.models.dto.TransactionExpenseLimitResponse;
import com.bank.app.paymentservice.models.entities.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

    @Query("""
            SELECT new com.bank.app.paymentservice.models.dto.TransactionExpenseLimitResponse(
                ac.account,
                pm.accountTo,
                pm.currencyShortName,
                pm.transactionSum,
                pm.expenseCategory,
                pm.dateTime,
                ex.limitSum,
                ex.limitDateTime,
                ex.currency
            ) FROM AccountInfo ac
            INNER JOIN PaymentTransaction pm ON ac.id = pm.accountFrom.id
            INNER JOIN ExpenseLimit ex ON pm.expenseCategory =ex.expenseCategory
            WHERE ac.account=:acc
            """)
    List<TransactionExpenseLimitResponse> getAllTransactionsByAccount(@Param("acc") String account);

    @Query("""
            SELECT new com.bank.app.paymentservice.models.dto.TransactionExpenseLimitResponse(
                ac.account,
                pm.accountTo,
                pm.currencyShortName,
                pm.transactionSum,
                pm.expenseCategory,
                pm.dateTime,
                ex.limitSum,
                ex.limitDateTime,
                ex.currency
            ) FROM AccountInfo ac
            INNER JOIN PaymentTransaction pm ON ac.id = pm.accountFrom.id
            INNER JOIN ExpenseLimit ex ON pm.expenseCategory =ex.expenseCategory
            WHERE ac.account=:account AND pm.limitExceeded = :limitEx
            """)
    List<TransactionExpenseLimitResponse> getAllByAccountAndLimitExceeded(@Param("account") String account,
                                                                          @Param("limitEx") boolean limitExceeded);
}
