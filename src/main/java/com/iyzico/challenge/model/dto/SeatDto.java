package com.iyzico.challenge.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SeatDto {
	
	private Long id;
	private String seatNumber;
	private Long customerId;
	private Long flightId;
	private BigDecimal price;
}
