package com.iyzico.challenge.service;

import com.iyzico.challenge.model.dto.SeatDto;

public interface SeatService {
	SeatDto saveSeat(String txnId, SaveSeatRequest request);
	Boolean deleteSeatById(String txnId, Long id);
	SeatDto updateSeat(String txnId, UpdateSeatRequest request);
}
