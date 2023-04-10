package com.iyzico.challenge.service;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PaymentServiceRequest {
	@NotNull
	private Long seatId;
	@NotNull
	private Long customerId;
}
