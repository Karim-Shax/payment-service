package com.bank.app.paymentservice.service;


import com.bank.app.paymentservice.models.converter.CurrencyMapper;
import com.bank.app.paymentservice.rest.client.CurrencyRateApiClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CurrencyExchangerService {

    private final CurrencyMapper currencyMapper;
    private final CurrencyRateApiClient rateApiClient;

    public CurrencyExchangerService(CurrencyMapper currencyMapper, CurrencyRateApiClient rateApiClient) {
        this.currencyMapper = currencyMapper;
        this.rateApiClient = rateApiClient;
    }

    public BigDecimal getAmountKztToUsd(BigDecimal amountKzt) {
        return amountKzt.divide(currencyMapper
                        .toBigDecimalKzt(rateApiClient.getJsonRatesFromApiLayer("KZT,RUB")), RoundingMode.HALF_UP);
    }

    public BigDecimal getAmountRubToUsd(BigDecimal amountRub) {
        return amountRub.divide(currencyMapper
                        .toBigDecimalRub(rateApiClient.getJsonRatesFromApiLayer("KZT,RUB")), RoundingMode.HALF_UP);
    }

}
