package com.bank.app.paymentservice.models.dto;

public record Response (
        int status,
        String message
) {
}
