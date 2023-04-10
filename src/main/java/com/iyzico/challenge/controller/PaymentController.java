package com.iyzico.challenge.controller;

import com.google.gson.Gson;
import com.iyzico.challenge.service.IyzicoPaymentService;
import com.iyzico.challenge.service.PaymentServiceRequest;
import com.iyzico.challenge.service.PaymentServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.ServiceUnavailableException;
import javax.validation.Valid;
import java.security.InvalidParameterException;
import java.util.UUID;

@Slf4j
@RequestMapping("/API/V1")
@RestController
public class PaymentController {
	
	public static final Gson gson = new Gson();
	
	@Autowired
	private IyzicoPaymentService paymentService;
	
	@RequestMapping(path = "/payment",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PaymentServiceResponse> paymentForSeatPrice(@Valid @RequestBody PaymentServiceRequest paymentServiceRequest){
		String txnId = UUID.randomUUID().toString();
		
		log.info("{} PaymentController - paymentForSeatPrice() start.", txnId);
		PaymentServiceResponse response=new PaymentServiceResponse();
		response.setTransactionId(txnId);
		try {
			response.setResult(paymentService.paymentForSeat(txnId,paymentServiceRequest));
			log.info("{} PaymentController - paymentForSeatPrice() finish: {}", txnId, gson.toJson(response));
			
		}  catch(InvalidParameterException ex){
			log.error("{} PaymentController - paymentForSeatPrice() has an error: {}", txnId, ex.toString());
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		} catch(ServiceUnavailableException ex){
			log.error("{} PaymentController - paymentForSeatPrice() has an error: {}", txnId, ex.toString());
			return new ResponseEntity(response, HttpStatus.SERVICE_UNAVAILABLE);
		}catch (Exception e) {
			log.error("{} PaymentController - paymentForSeatPrice() has an error: {}", txnId, e.toString());
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.ok(response);
	}
}
