package com.iyzico.challenge.service;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SaveFlightRequest {
	@NotBlank
	private String flightName;
	@NotBlank
	private String description;
}
