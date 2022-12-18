package com.bank.app.paymentservice;


import com.bank.app.paymentservice.models.entities.ExpenseLimit;
import com.bank.app.paymentservice.models.enums.ExpenseCategory;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ClientRestControllerTest extends SpringSimpleContextTest {


    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
            "/db/script/client_rest_controller/insert.sql"
    })
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
            "/db/script/client_rest_controller/clear.sql"
    })
    /*Проверка получения всех транзакции по аккаунту и флагу превышения лимита*/
    public void getPaymentTransactionsByAccountAndLimitExceeded() throws Exception {
        mockMvc.perform(get("http://localhost:5555/api/client/transaction/{limitExceeded}", true)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("account", "12345678910"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].accountFrom", Is.is("12345678910")))
                .andExpect(jsonPath("$.[0].accountTo", Is.is("2222222222")))
                .andExpect(jsonPath("$.[0].currencyShortName", Is.is("RUB")))
                .andExpect(jsonPath("$.[0].sum", Is.is(23000.00)))
                .andExpect(jsonPath("$.[0].expenseCategory", Is.is("SERVICE")))
                .andExpect(jsonPath("$.[0].dateTime", Is.is("2022-12-14 10:10:24+06")))
                .andExpect(jsonPath("$.[0].limitSum", Is.is(2000.00)))
                .andExpect(jsonPath("$.[0].limitDateTime", Is.is("2022-12-11 10:00:24+06")))
                .andExpect(jsonPath("$.[0].limitCurrencyShortName", Is.is("USD")))

                .andExpect(jsonPath("$.[1].accountFrom", Is.is("12345678910")))
                .andExpect(jsonPath("$.[1].accountTo", Is.is("3333333333")))
                .andExpect(jsonPath("$.[1].currencyShortName", Is.is("KZT")))
                .andExpect(jsonPath("$.[1].sum", Is.is(25000.00)))
                .andExpect(jsonPath("$.[1].expenseCategory", Is.is("PRODUCT")))
                .andExpect(jsonPath("$.[1].dateTime", Is.is("2022-12-14 10:10:24+06")))
                .andExpect(jsonPath("$.[1].limitSum", Is.is(1000.00)))
                .andExpect(jsonPath("$.[1].limitDateTime", Is.is("2022-12-11 11:00:24+06")))
                .andExpect(jsonPath("$.[1].limitCurrencyShortName", Is.is("USD")));


        mockMvc.perform(get("http://localhost:5555/api/client/transaction/{limitExceeded}", false)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("account", "12345678910"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].accountFrom", Is.is("12345678910")))
                .andExpect(jsonPath("$.[0].accountTo", Is.is("4444444444")))
                .andExpect(jsonPath("$.[0].currencyShortName", Is.is("RUB")))
                .andExpect(jsonPath("$.[0].sum", Is.is(25000.00)))
                .andExpect(jsonPath("$.[0].expenseCategory", Is.is("SERVICE")))
                .andExpect(jsonPath("$.[0].dateTime", Is.is("2022-12-14 10:11:24+06")))
                .andExpect(jsonPath("$.[0].limitSum", Is.is(2000.00)))
                .andExpect(jsonPath("$.[0].limitDateTime", Is.is("2022-12-11 10:00:24+06")))
                .andExpect(jsonPath("$.[0].limitCurrencyShortName", Is.is("USD")))

                .andExpect(jsonPath("$.[1].accountFrom", Is.is("12345678910")))
                .andExpect(jsonPath("$.[1].accountTo", Is.is("1111111111")))
                .andExpect(jsonPath("$.[1].currencyShortName", Is.is("KZT")))
                .andExpect(jsonPath("$.[1].sum", Is.is(25000.00)))
                .andExpect(jsonPath("$.[1].expenseCategory", Is.is("PRODUCT")))
                .andExpect(jsonPath("$.[1].dateTime", Is.is("2022-12-14 10:00:24+06")))
                .andExpect(jsonPath("$.[1].limitSum", Is.is(1000.00)))
                .andExpect(jsonPath("$.[1].limitDateTime", Is.is("2022-12-11 11:00:24+06")))
                .andExpect(jsonPath("$.[1].limitCurrencyShortName", Is.is("USD")));


        mockMvc.perform(get("http://localhost:5555/api/client/transaction/{limitExceeded}", false)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("account", "99999999999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].accountFrom", Is.is("99999999999")))
                .andExpect(jsonPath("$.[0].accountTo", Is.is("5555555555")))
                .andExpect(jsonPath("$.[0].currencyShortName", Is.is("KZT")))
                .andExpect(jsonPath("$.[0].sum", Is.is(25000.0)))
                .andExpect(jsonPath("$.[0].expenseCategory", Is.is("PRODUCT")))
                .andExpect(jsonPath("$.[0].dateTime", Is.is("2022-12-14 10:12:24+06")))
                .andExpect(jsonPath("$.[0].limitSum", Is.is(3000.0)))
                .andExpect(jsonPath("$.[0].limitDateTime", Is.is("2022-12-12 11:00:24+06")))
                .andExpect(jsonPath("$.[0].limitCurrencyShortName", Is.is("USD")));

        mockMvc.perform(get("http://localhost:5555/api/client/transaction/{limitExceeded}", false)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("account", "6568168168556"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", Is.is(400)))
                .andExpect(jsonPath("$.message", Is.is("Аккаунт по номеру 6568168168556 не найден")));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
            "/db/script/client_rest_controller/insert.sql"
    })
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
            "/db/script/client_rest_controller/clear.sql"
    })
    /*Проверка изменения суммы лимита */
    public void setLimitSumByAccountAndCategory() throws Exception {
        mockMvc.perform(put("http://localhost:5555/api/client/limit?sum={sum}&category={category}", 1236.00, "PRODUCT")
                        .header("account", "99999999999"))
                .andExpect(jsonPath("$.status", Is.is(200)))
                .andExpect(jsonPath("$.message", Is.is("Лимит успешно изменён")));

        ExpenseLimit expenseLimit = entityManager.createQuery("""
                        SELECT exp FROM ExpenseLimit exp
                        WHERE exp.expenseCategory=:category
                        AND exp.accountInfo.id=:accId
                        """, ExpenseLimit.class)
                .setParameter("category", ExpenseCategory.PRODUCT)
                .setParameter("accId", 2L).getSingleResult();
        Assertions.assertEquals(expenseLimit.getLimitSum(), new BigDecimal("1236.00"));
        Assertions.assertEquals(expenseLimit.getLimitTemporary(), new BigDecimal("1236.00"));

        mockMvc.perform(put("http://localhost:5555/api/client/limit?sum={sum}&category={category}", 1236.00, "PRODUCT")
                        .header("account", "5663324562788"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", Is.is(400)))
                .andExpect(jsonPath("$.message", Is.is("Лимит по номеру 5663324562788 и категории PRODUCT не найден")));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
            "/db/script/client_rest_controller/insert.sql"
    })
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
            "/db/script/client_rest_controller/clear.sql"
    })
    /*Проверка на получение всех лимитов по аккаунту*/
    public void getAllLimitsByAccount() throws Exception {
        mockMvc.perform(get("http://localhost:5555/api/client/limit/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("account", "6568168168556"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", Is.is(400)))
                .andExpect(jsonPath("$.message", Is.is("Аккаунт по номеру 6568168168556 не найден")));

        mockMvc.perform(get("http://localhost:5555/api/client/limit/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("account", "12345678910"))
                .andExpect(jsonPath("$.[0].currency", Is.is("USD")))
                .andExpect(jsonPath("$.[0].limitDateTime", Is.is("2022-12-11 11:00:24+06")))
                .andExpect(jsonPath("$.[0].limitSum", Is.is(1000.0)))
                .andExpect(jsonPath("$.[0].expenseCategory", Is.is("PRODUCT")))

                .andExpect(jsonPath("$.[1].currency", Is.is("USD")))
                .andExpect(jsonPath("$.[1].limitDateTime", Is.is("2022-12-11 10:00:24+06")))
                .andExpect(jsonPath("$.[1].limitSum", Is.is(2000.0)))
                .andExpect(jsonPath("$.[1].expenseCategory", Is.is("SERVICE")));
    }


}
