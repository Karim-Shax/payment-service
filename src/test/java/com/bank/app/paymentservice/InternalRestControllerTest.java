package com.bank.app.paymentservice;

import com.bank.app.paymentservice.models.dto.PaymentTransactionRequestDto;
import com.bank.app.paymentservice.models.entities.PaymentTransaction;
import com.bank.app.paymentservice.models.enums.CurrencyShortName;
import com.bank.app.paymentservice.models.enums.ExpenseCategory;
import com.bank.app.paymentservice.rest.client.CurrencyRateApiClient;
import com.bank.app.paymentservice.util.DateTimeFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBeans({@MockBean(CurrencyRateApiClient.class)})
public class InternalRestControllerTest extends SpringSimpleContextTest {

    @Autowired
    private CurrencyRateApiClient client;

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
            "/db/script/insert.sql"
    })
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
            "/db/script/clear.sql"
    })
    public void saveTransactionAndProcessExpenseLimitTest() throws Exception {
        doReturn("""
                                {
                                  "change": true,
                                  "end_date": "2022-12-12",
                                  "quotes": {
                                    "USDKZT": {
                                      "change": 0.4013,
                                      "change_pct": 0.0853,
                                      "end_rate": 470.686958,
                                      "start_rate": 470.285633
                                    },
                                    "USDRUB": {
                                      "change": -0.8887,
                                      "change_pct": -1.4016,
                                      "end_rate": 62.515038,
                                      "start_rate": 63.40369
                                    }
                                  },
                                  "source": "USD",
                                  "start_date": "2022-12-09",
                                  "success": true
                                }
                """).when(client).getJsonRatesFromApiLayer("KZT,RUB");

        PaymentTransactionRequestDto paymentTransactionRequestDto =
                new PaymentTransactionRequestDto(
                        "12345678910",
                        "12345678912",
                        CurrencyShortName.RUB,
                        new BigDecimal("25000.00"),
                        ExpenseCategory.PRODUCT,
                        "2022-12-15 18:00:24+06"
                );

        mockMvc.perform(post("http://localhost:5555/api/internal/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentTransactionRequestDto)))
                .andExpect(status().isOk());
        PaymentTransaction paymentTransaction = entityManager.createQuery("""
                        SELECT p FROM PaymentTransaction p
                        WHERE p.accountFrom.id = :accId
                        AND p.expenseCategory = :exp
                        """, PaymentTransaction.class)
                .setParameter("accId", 1L)
                .setParameter("exp", paymentTransactionRequestDto.expenseCategory())
                .getSingleResult();
        Assertions.assertEquals(paymentTransactionRequestDto.accountTo(), paymentTransaction.getAccountTo());
        Assertions.assertEquals(paymentTransactionRequestDto.sum(), paymentTransaction.getTransactionSum());
        Assertions.assertEquals(paymentTransactionRequestDto.expenseCategory(), paymentTransaction.getExpenseCategory());
        Assertions.assertEquals(paymentTransactionRequestDto.dateTime(), paymentTransaction.getDateTime().format(DateTimeFormatter.format()));

        BigDecimal limitTemporary = entityManager.createQuery("""
                        SELECT exp.limitTemporary FROM ExpenseLimit exp
                        WHERE exp.accountInfo.account=:acc
                        AND exp.expenseCategory = :exp
                        """, BigDecimal.class)
                .setParameter("acc", paymentTransactionRequestDto.accountFrom())
                .setParameter("exp", paymentTransactionRequestDto.expenseCategory()).getSingleResult();

        Assertions.assertEquals(limitTemporary, new BigDecimal("600.10"));
    }

}
