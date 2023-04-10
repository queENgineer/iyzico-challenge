package com.iyzico.challenge.service;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PaymentServiceResponse {
	private String transactionId;
	private Boolean result;
}
