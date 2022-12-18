package com.bank.app.paymentservice.service;


import com.bank.app.paymentservice.repository.ExpenseLimitRepository;
import com.bank.app.paymentservice.rest.client.CurrencyRateApiRestClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledActionsService {

    private final CurrencyRateApiRestClient currencyRateApiRestClient;
    private final ExpenseLimitRepository expenseLimitRepository;

    public ScheduledActionsService(CurrencyRateApiRestClient currencyRateApiRestClient, ExpenseLimitRepository expenseLimitRepository) {
        this.currencyRateApiRestClient = currencyRateApiRestClient;
        this.expenseLimitRepository = expenseLimitRepository;
    }


    //метод будет запускаться каждый день в 16 30
    //обновляет остаток лимита на сумму лимита
    @Scheduled(cron = "${interval-in-cron-daily}")
    private void sendRequestGetRatesAndSave() {
         currencyRateApiRestClient.getJsonRatesFromApiLayer("KZT,RUB");
    }
    //метод будет запускаться каждый месяц первого числа в 00 часов 1 минут
    @Scheduled(cron = "${interval-in-cron-monthly}")
    private void processingExpenseLimit() {
        expenseLimitRepository.restoreAllLimits();
    }
}
