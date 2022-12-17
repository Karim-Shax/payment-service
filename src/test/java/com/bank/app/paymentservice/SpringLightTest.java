package com.bank.app.paymentservice;

import com.bank.app.paymentservice.models.converter.CurrencyMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;


@SpringBootTest
public abstract class SpringLightTest {

    @Autowired
    private CurrencyMapper currencyMapper;

    @Test
    public void currencyMapperTest() {
        String json = """
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
                """;

        Assertions.assertEquals(BigDecimal.valueOf(62.515038), currencyMapper.toBigDecimalRub(json));
        Assertions.assertEquals(BigDecimal.valueOf(470.686958), currencyMapper.toBigDecimalKzt(json));
    }
}
