package com.bank.app.paymentservice.util;

import java.time.format.DateTimeFormatterBuilder;

public class DateTimeFormatter {

    public static java.time.format.DateTimeFormatter format() {
        return new DateTimeFormatterBuilder()
                .append(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX")).toFormatter();
    }
}
