package com.iyzico.challenge.controller;


import com.google.gson.Gson;
import com.iyzico.challenge.model.dto.SeatDto;
import com.iyzico.challenge.service.DeleteSeatResponse;
import com.iyzico.challenge.service.SaveSeatRequest;
import com.iyzico.challenge.service.SaveSeatResponse;
import com.iyzico.challenge.service.SeatService;
import com.iyzico.challenge.service.UpdateSeatRequest;
import com.iyzico.challenge.service.UpdateSeatResponse;
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
import java.security.InvalidParameterException;
import java.util.UUID;

@Slf4j
@RequestMapping("/API/V1")
@RestController
public class SeatControlller {
	public static final Gson gson = new Gson();
	
	@Autowired
	private SeatService seatService;
	
	@RequestMapping(path = "/seat",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SaveSeatResponse> saveSeat(@Valid @RequestBody SaveSeatRequest saveSeatRequest){
		String txnId = UUID.randomUUID().toString();
		
		log.info("{} SeatController - saveSeat() start.", txnId);
		SaveSeatResponse response=new SaveSeatResponse();
		try {
			SeatDto seatDto=seatService.saveSeat(txnId,saveSeatRequest);
			response.setSeatDto(seatDto);
			response.setTransactionId(txnId);
			log.info("{} SeatController - saveSeat() finish: {}", txnId, gson.toJson(response));
			
		} catch(InvalidParameterException ex){
			log.error("{} SeatController - saveSeat() has an error: {}", txnId, ex.toString());
			ResponseEntity.badRequest();
		} catch (ConstraintViolationException ex){
			log.error("{} SeatController - saveSeat() has an error: {}", txnId, ex.toString());
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error("{} SeatController - saveSeat() has an error: {}", txnId, e.toString());
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.ok(response);
	}
	
	@RequestMapping(path = "/seat",method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UpdateSeatResponse> updateSeat(@Valid @RequestBody UpdateSeatRequest updateFlightRequest){
		String txnId = UUID.randomUUID().toString();
		
		log.info("{} SeatController - updateSeat() start.", txnId);
		UpdateSeatResponse response=new UpdateSeatResponse();
		response.setTransactionId(txnId);
		try {
			SeatDto seatDto=seatService.updateSeat(txnId,updateFlightRequest);
			response.setSeatDto(seatDto);
			log.info("{} SeatController - updateSeat() finish: {}", txnId, gson.toJson(response));
			
		} catch (ConstraintViolationException ex){
			log.error("{} SeatController - updateSeat() has an error: {}", txnId, ex.toString());
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error("{} SeatController - updateSeat() has an error: {}", txnId, e.toString());
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.ok(response);
	}
	
	@RequestMapping(path = "/seat/{id}",method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DeleteSeatResponse> deleteSeat(@PathVariable("id") Long id ){
		String txnId = UUID.randomUUID().toString();
		
		log.info("{} SeatController - deleteSeat() start.", txnId);
		DeleteSeatResponse response=new DeleteSeatResponse();
		response.setTransactionId(txnId);
		try {
			response.setResponse(seatService.deleteSeatById(txnId,id));
			log.info("{} SeatController - deleteSeat() finish: {}", txnId, gson.toJson(response));
		} catch (Exception e) {
			log.error("{} SeatController - deleteSeat() has an error: {}", txnId, e.toString());
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.ok(response);
	}
}
