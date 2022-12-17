package com.bank.app.paymentservice.config;

import com.bank.app.paymentservice.exeptions.LimitExceededException;
import com.bank.app.paymentservice.exeptions.NotFoundException;
import com.bank.app.paymentservice.models.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Response> handle(NotFoundException e) {
        return ResponseEntity.status(400).body(new Response(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Response> handle(LimitExceededException e) {
        return ResponseEntity.ok(new Response(HttpStatus.OK.value(), e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Response> handle(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body(new Response(400, "Неверные данные"));
    }

    @ExceptionHandler
    public ResponseEntity<Response> handle(HttpMessageNotReadableException exception){
        return ResponseEntity.badRequest().body(new Response(400, "Неверные данные"));
    }
}

