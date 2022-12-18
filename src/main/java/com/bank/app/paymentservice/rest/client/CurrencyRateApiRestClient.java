package com.bank.app.paymentservice.rest.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;


@Service
@Slf4j
public class CurrencyRateApiRestClient {
    @Autowired
    private Environment env;

    //currencies нужно передавать в виде одной строки через запятую например KZT,RUB
    @Cacheable("dailyRate")
    public String getJsonRatesFromApiLayer(String currencies) {

        //это проперти для тестовых запусков, поскольку количество запросов ограничено,
        //чтобы включить нужно указать true в rate.api.enabled
        if (Objects.equals(env.getProperty("rate.api.enabled"), "true")) {
            HttpRequest request = null;
            try {
                request = HttpRequest.newBuilder()
                        .uri(new URI("https://api.apilayer.com/currency_data/change?currencies=" + currencies + "&start_date="
                                + LocalDate.now().minusDays(2).format(DateTimeFormatter.ISO_DATE) +
                                "&end_date="
                                + LocalDate.now().format(DateTimeFormatter.ISO_DATE)))
                        .setHeader("apikey", "aNlUV2CUTyrVJbE150k7QyAHuvmF9rAC")
                        .version(HttpClient.Version.HTTP_2)
                        .timeout(Duration.of(10, ChronoUnit.SECONDS))
                        .GET()
                        .build();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            HttpResponse<String> response = null;
            try {
                response = HttpClient
                        .newBuilder()
                        .proxy(ProxySelector.getDefault())
                        .build().send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            return response.body();
        }

        return """
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
    }
}

