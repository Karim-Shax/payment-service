package com.bank.app.paymentservice;

import com.bank.app.paymentservice.models.dto.PaymentTransactionRequestDto;
import com.bank.app.paymentservice.models.entities.PaymentTransaction;
import com.bank.app.paymentservice.models.enums.CurrencyShortName;
import com.bank.app.paymentservice.models.enums.ExpenseCategory;
import com.bank.app.paymentservice.rest.client.CurrencyRateApiRestClient;
import com.bank.app.paymentservice.util.DateTimeFormatter;
import org.hamcrest.core.Is;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBeans({@MockBean(CurrencyRateApiRestClient.class)})
public class InternalRestControllerTest extends SpringSimpleContextTest {

    @Autowired
    private CurrencyRateApiRestClient client;

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
            "/db/script/internal_rest_controller/insert.sql"
    })
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
            "/db/script/internal_rest_controller/clear.sql"
    })
    /*Проверка сохранения транзакции и изменения лимита*/
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

        PaymentTransactionRequestDto paymentTransactionRequestDto2 =
                new PaymentTransactionRequestDto(
                        "1234336566565",
                        "12345678912",
                        CurrencyShortName.RUB,
                        new BigDecimal("25000.00"),
                        ExpenseCategory.PRODUCT,
                        "2022-12-15 18:00:24+06"
                );

        mockMvc.perform(post("http://localhost:5555/api/internal/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentTransactionRequestDto2)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", Is.is(400)))
                .andExpect(jsonPath("$.message", Is.is("Аккаунт по номеру 1234336566565 не найден")));
    }


    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
            "/db/script/internal_rest_controller/insert.sql"
    })
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
            "/db/script/internal_rest_controller/clear.sql"
    })
    /*Проверка сохранения нескольких транзакции и изменения лимита*/
    public void saveMoreTransactionsAndProcessExpenseLimitsTest() throws Exception {
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
                        CurrencyShortName.KZT,
                        new BigDecimal("25000.00"),
                        ExpenseCategory.SERVICE,
                        "2022-12-15 18:00:24+06"
                );

        mockMvc.perform(post("http://localhost:5555/api/internal/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentTransactionRequestDto)))
                .andExpect(status().isOk());
        mockMvc.perform(post("http://localhost:5555/api/internal/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentTransactionRequestDto)))
                .andExpect(status().isOk());
        mockMvc.perform(post("http://localhost:5555/api/internal/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentTransactionRequestDto)))
                .andExpect(status().isOk());

        Assertions.assertEquals(entityManager.createQuery("""
                                SELECT exp.limitTemporary FROM ExpenseLimit exp
                                WHERE exp.accountInfo.account=:acc
                                AND exp.expenseCategory = :exp
                                """, BigDecimal.class)
                        .setParameter("acc", paymentTransactionRequestDto.accountFrom())
                        .setParameter("exp", paymentTransactionRequestDto.expenseCategory()).getSingleResult(),
                new BigDecimal("840.67"));


        paymentTransactionRequestDto = new PaymentTransactionRequestDto("12345678910",
                "12345678912",
                CurrencyShortName.RUB,
                new BigDecimal("25000.00"),
                ExpenseCategory.PRODUCT,
                "2022-12-15 18:00:24+06");

        mockMvc.perform(post("http://localhost:5555/api/internal/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentTransactionRequestDto)))
                .andExpect(status().isOk());
        mockMvc.perform(post("http://localhost:5555/api/internal/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentTransactionRequestDto)))
                .andExpect(status().isOk());
        mockMvc.perform(post("http://localhost:5555/api/internal/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentTransactionRequestDto)))
                .andExpect(status().isOk());

        Assertions.assertEquals(entityManager.createQuery("""
                                SELECT exp.limitTemporary FROM ExpenseLimit exp
                                WHERE exp.accountInfo.account=:acc
                                AND exp.expenseCategory = :exp
                                """, BigDecimal.class)
                        .setParameter("acc", paymentTransactionRequestDto.accountFrom())
                        .setParameter("exp", paymentTransactionRequestDto.expenseCategory()).getSingleResult(),
                new BigDecimal("200.20"));

        Assertions.assertNotNull(entityManager.createQuery("""
                SELECT pm FROM PaymentTransaction  pm
                WHERE pm.limitExceeded = true AND pm.expenseCategory = 'PRODUCT'
                """).getSingleResult());
    }

}
