package com.iyzico.challenge.service;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class SaveSeatRequest {
	@NotBlank
	private String seatNumber;
	@NotNull
	private Long flightId;
	@NotNull
	private BigDecimal price;
}
