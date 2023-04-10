package com.iyzico.challenge.service;

import com.iyzico.challenge.model.dto.SeatDto;
import lombok.Data;

@Data
public class UpdateSeatResponse {
	private String transactionId;
	private SeatDto seatDto;
}
