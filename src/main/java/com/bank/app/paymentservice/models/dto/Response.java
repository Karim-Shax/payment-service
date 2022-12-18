package com.bank.app.paymentservice.models.dto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Информация кастомного ответа")
public record Response (
        int status,
        String message
) {
}
