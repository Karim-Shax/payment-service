package com.bank.app.paymentservice.service;


import com.bank.app.paymentservice.repository.ExpenseLimitRepository;
import com.bank.app.paymentservice.rest.client.CurrencyRateApiClient;
import com.bank.app.paymentservice.service.abstracts.AccountInfoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledActionsService {

    private final CurrencyRateApiClient currencyRateApiClient;
    private final AccountInfoService accountInfoService;
    private final ExpenseLimitRepository expenseLimitRepository;

    public ScheduledActionsService(CurrencyRateApiClient currencyRateApiClient, AccountInfoService accountInfoService, ExpenseLimitRepository expenseLimitRepository) {
        this.accountInfoService = accountInfoService;
        this.currencyRateApiClient = currencyRateApiClient;
        this.expenseLimitRepository = expenseLimitRepository;
    }
    /*@Scheduled(cron = "${interval-in-cron-daily}")
    private void sendRequestGetRatesAndSave() {
         currencyRateApiClient.getJsonRatesFromApiLayer("KZT,RUB");
    }

    @Scheduled(cron = "${interval-in-cron-monthly}")
    private void processingExpenseLimit() {
        expenseLimitRepository.restoreAllLimits();
    }*/
}
