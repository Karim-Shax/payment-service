package com.bank.app.paymentservice.models.converter;

import com.bank.app.paymentservice.models.enums.CurrencyShortName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyMapper {
    private ObjectMapper objectMapper;

    public CurrencyMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public BigDecimal toBigDecimalKzt(String json) {
        return getRatesFromJson(json, CurrencyShortName.KZT, "end_rate");
    }


    public BigDecimal toBigDecimalRub(String json) {
        return getRatesFromJson(json, CurrencyShortName.RUB, "end_rate");
    }

    private BigDecimal getRatesFromJson(String json, CurrencyShortName currencyShortName, String rate_date) {
        BigDecimal rate;
        try {
            rate = new BigDecimal(objectMapper
                    .readTree(json)
                    .get("quotes")
                    .get("USD" + currencyShortName)
                    .get(rate_date)
                    .asText());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return rate;
    }
}
