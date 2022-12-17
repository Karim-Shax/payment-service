package com.bank.app.paymentservice;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;


@Slf4j
public class CurrencyCacheServiceTest extends SpringSimpleContextTest {

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
            "/db/script/insert.sql"
    })
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
            "/db/script/clear.sql"
    })
    public void saveTransactionAndProcessExpenseLimitTest() {
        entityManager.createQuery("select c from PaymentTransaction  c");
    }

}
