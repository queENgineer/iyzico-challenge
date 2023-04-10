package com.iyzico.challenge.service;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateFlightRequest {
	@NotNull
	private Long id;
	
	private String flightName;
	
	private String description;
}
