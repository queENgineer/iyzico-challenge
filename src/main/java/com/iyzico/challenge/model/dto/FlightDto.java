package com.iyzico.challenge.model.dto;


import lombok.Data;

import java.util.List;

@Data
public class FlightDto {
	
	private Long id;
	private String flightName;
	private String description;
	private List<SeatDto> seatDtoList;
}
