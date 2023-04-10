package com.iyzico.challenge.controller;


import com.google.gson.Gson;
import com.iyzico.challenge.model.dto.FlightDto;
import com.iyzico.challenge.service.DeleteFlightResponse;
import com.iyzico.challenge.service.FlightService;
import com.iyzico.challenge.service.GetFlightListResponse;
import com.iyzico.challenge.service.SaveFlightRequest;
import com.iyzico.challenge.service.SaveFlightResponse;
import com.iyzico.challenge.service.UpdateFlightRequest;
import com.iyzico.challenge.service.UpdateFlightResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;


@Slf4j
@RequestMapping("/API/V1")
@RestController
public class FlightController {
	
	public static final Gson gson = new Gson();
	
	@Autowired
	private FlightService flightService;
	
	@RequestMapping(path = "/flight",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SaveFlightResponse> saveFlight(@Valid @RequestBody SaveFlightRequest saveFlightRequest){
		String txnId = UUID.randomUUID().toString();
		
		log.info("{} FlightController - saveFlight() start.", txnId);
		SaveFlightResponse response=new SaveFlightResponse();
		try {
			FlightDto flightDto=flightService.saveFlight(txnId,saveFlightRequest);
			response.setFlightDto(flightDto);
			response.setTransactionId(txnId);
			log.info("{} FlightController - saveFlight() finish: {}", txnId, gson.toJson(response));
			
		}catch (ConstraintViolationException ex){
			log.error("{} FlightController - saveFlight() has an error: {}", txnId, ex.toString());
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception e) {
			log.error("{} FlightController - saveFlight() has an error: {}", txnId, e.toString());
			ResponseEntity.internalServerError();
		}
		return ResponseEntity.ok(response);
	}
	
	@RequestMapping(path = "/flight",method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UpdateFlightResponse> updateFlight(@Valid @RequestBody UpdateFlightRequest updateFlightRequest){
		String txnId = UUID.randomUUID().toString();
		
		log.info("{} FlightController - updateFlight() start.", txnId);
		UpdateFlightResponse response=new UpdateFlightResponse();
		response.setTransactionId(txnId);
		try {
			FlightDto flightDto=flightService.updateFlight(txnId,updateFlightRequest);
			response.setFlightDto(flightDto);
			log.info("{} FlightController - updateFlight() finish: {}", txnId, gson.toJson(response));
			
		} catch (ConstraintViolationException ex){
			log.error("{} FlightController - updateFlight() has an error: {}", txnId, ex.toString());
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}catch (Exception e) {
			log.error("{} FlightController - updateFlight() has an error: {}", txnId, e.toString());
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.ok(response);
	}
	
	@RequestMapping(path = "/flight/{id}",method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DeleteFlightResponse> deleteFlight(@PathVariable("id") Long id ){
		String txnId = UUID.randomUUID().toString();
		
		log.info("{} FlightController - deleteFlight() start.", txnId);
		DeleteFlightResponse response=new DeleteFlightResponse();
		response.setTransactionId(txnId);
		try {
			response.setResponse(flightService.deleteFlightById(txnId,id));
			log.info("{} FlightController - deleteFlight() finish: {}", txnId, gson.toJson(response));
		} catch (Exception e) {
			log.error("{} FlightController - deleteFlight() has an error: {}", txnId, e.toString());
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.ok(response);
	}
	@RequestMapping(path = "/flightList",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetFlightListResponse> getFlightList(){
		String txnId = UUID.randomUUID().toString();
		
		log.info("{} FlightController - getFlightList() start.", txnId);
		GetFlightListResponse response=new GetFlightListResponse();
		response.setTransactionId(txnId);
		try {
			response.setFlightList(flightService.getFlightList(txnId));
			log.info("{} FlightController - getFlightList() finish: {}", txnId, gson.toJson(response));
			
		} catch (Exception e) {
			log.error("{} FlightController - getFlightList() has an error: {}", txnId, e.toString());
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.ok(response);
	}
}
