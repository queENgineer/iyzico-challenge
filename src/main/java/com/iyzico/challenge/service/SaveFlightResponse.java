package com.iyzico.challenge.service;

import com.iyzico.challenge.model.dto.FlightDto;
import lombok.Data;

@Data
public class SaveFlightResponse {
	private String transactionId;
	private FlightDto flightDto;
}
