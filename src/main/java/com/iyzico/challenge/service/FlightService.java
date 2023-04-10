package com.iyzico.challenge.service;

import com.iyzico.challenge.model.dto.FlightDto;

import java.util.List;

public interface FlightService {
	FlightDto saveFlight(String txnId, SaveFlightRequest request);
	Boolean deleteFlightById(String txnId, Long id);
	FlightDto updateFlight(String txnId, UpdateFlightRequest request);
	List<FlightDto> getFlightList(String txnId);
}
