package com.bank.app.paymentservice.repository;

import com.bank.app.paymentservice.models.entities.ExpenseLimit;
import com.bank.app.paymentservice.models.enums.CurrencyShortName;
import com.bank.app.paymentservice.models.enums.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ExpenseLimitRepository extends JpaRepository<ExpenseLimit, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "4000")})
    @Query("""
            SELECT exp FROM ExpenseLimit exp
            WHERE exp.accountInfo.account = :ac
            AND exp.currency = :cur
            AND exp.expenseCategory = :cat
            """)
    Optional<ExpenseLimit> finByAccountAndCurrencyAndCategory(@Param("ac") String account,
                                                              @Param("cur") CurrencyShortName currencyShortName,
                                                              @Param("cat") ExpenseCategory expenseCategory);

    @Modifying
    @Query("""
            UPDATE ExpenseLimit exp
            SET exp.limitSum = :sum, exp.limitTemporary = :sum, exp.limitDateTime = CURRENT_TIMESTAMP
            WHERE exp.accountInfo IN (SELECT ac FROM AccountInfo ac WHERE ac.account = :account)
            AND exp.expenseCategory = :category
            """)
    void setLimitSumByAccountAndCategory(
            @Param("sum") BigDecimal sum,
            @Param("account") String account,
            @Param("category") ExpenseCategory expenseCategory);


    @Query("SELECT el FROM ExpenseLimit el WHERE el.accountInfo.account=:acc")
    List<ExpenseLimit> findAllByAccount(@Param("acc") String account);

    @Query("""
            SELECT CASE WHEN (COUNT(ex) > 0) THEN TRUE ELSE FALSE END 
            FROM ExpenseLimit ex 
            WHERE ex.accountInfo.account=:account 
            AND ex.expenseCategory = :category
            """)
    boolean isExistByAccountAndCategory(@Param("account") String account, @Param("category") ExpenseCategory expenseCategory);


    @Modifying
    @Query(value = """
            update expense_limits
            set limit_temporary = limit_sum
            where limit_temporary != limit_sum
            """, nativeQuery = true)
    void restoreAllLimits();
}
