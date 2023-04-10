package com.iyzico.challenge.service;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IyzicoPaymentIntegrationTest {
	@Autowired
	private TestRestTemplate restTemplate;
	
	
	
	@Sql({ "create-db.sql", "insert-data.sql" })
	@Test
	public void testSuccessfulIyzicoPayment(){
		PaymentServiceRequest paymentServiceRequest=new PaymentServiceRequest();
		paymentServiceRequest.setCustomerId(12345L);
		paymentServiceRequest.setSeatId(1L);
		HttpEntity<?> httpEntity = new HttpEntity<Object>(paymentServiceRequest);
		ResponseEntity<PaymentServiceResponse> responseEntity = restTemplate.exchange(
			"/API/V1/payment", HttpMethod.POST, httpEntity,
			new ParameterizedTypeReference<PaymentServiceResponse>(){});
		Boolean paymentResponse = responseEntity.getBody().getResult();
		Assert.assertEquals(true,paymentResponse);
	
	}
	
	@Sql({ "create-db.sql", "insert-data.sql" })
	@Test
	public void testIyzicoPaymentThrowsInvalidParameterExceptionBecauseOfInvalidSeatId(){
		PaymentServiceRequest paymentServiceRequest=new PaymentServiceRequest();
		paymentServiceRequest.setCustomerId(12345L);
		paymentServiceRequest.setSeatId(155L);
		HttpEntity<?> httpEntity = new HttpEntity<Object>(paymentServiceRequest);
		ResponseEntity<PaymentServiceResponse> responseEntity = restTemplate.exchange(
			"/API/V1/payment", HttpMethod.POST, httpEntity,
			new ParameterizedTypeReference<PaymentServiceResponse>(){});
	
		Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		
	}
	
	@Sql({ "create-db.sql", "insert-data.sql" })
	@Test
	public void testIyzicoPaymentThrowsInvalidParameterExceptionBecauseOfAlreadyBookedSeat(){
		PaymentServiceRequest paymentServiceRequest=new PaymentServiceRequest();
		paymentServiceRequest.setCustomerId(222L);
		paymentServiceRequest.setSeatId(1111L);
		HttpEntity<?> httpEntity = new HttpEntity<Object>(paymentServiceRequest);
		ResponseEntity<PaymentServiceResponse> responseEntity = restTemplate.exchange(
			"/API/V1/payment", HttpMethod.POST, httpEntity,
			new ParameterizedTypeReference<PaymentServiceResponse>(){});
		
		Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		
	}
}
