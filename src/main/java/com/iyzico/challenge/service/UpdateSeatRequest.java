package com.iyzico.challenge.service;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class UpdateSeatRequest {
	@NotNull
	private Long id;
	private String seatNumber;
	private Long flightId;
	private BigDecimal price;
}
