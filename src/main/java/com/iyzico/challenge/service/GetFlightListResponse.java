package com.iyzico.challenge.service;


import com.iyzico.challenge.model.dto.FlightDto;
import lombok.Data;

import java.util.List;

@Data
public class GetFlightListResponse {
	private String transactionId;
	private List<FlightDto> flightList;
}
