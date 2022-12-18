package com.bank.app.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;


@SpringBootApplication
/*@EnableEurekaClient*/
public class PaymentServiceApplication {
	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+6:00"));
		SpringApplication.run(PaymentServiceApplication.class, args);
	}
}
