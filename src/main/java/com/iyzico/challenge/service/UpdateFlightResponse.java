package com.iyzico.challenge.service;

import com.iyzico.challenge.model.dto.FlightDto;
import com.iyzico.challenge.model.dto.SeatDto;
import lombok.Data;

@Data
public class UpdateFlightResponse {
	private String transactionId;
	private FlightDto flightDto;
}
