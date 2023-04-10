package com.iyzico.challenge.service;


import com.iyzico.challenge.model.dto.SeatDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SeatControllerIntegrationTest {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Sql({ "create-db.sql", "insert-data.sql" })
	@Test
	public void testSaveSeat()
	{
		SaveSeatRequest saveSeatRequest=new SaveSeatRequest();
		saveSeatRequest.setFlightId(1L);
		saveSeatRequest.setSeatNumber("A4");
		saveSeatRequest.setPrice(new BigDecimal(100));
		HttpEntity<?> httpEntity = new HttpEntity<Object>(saveSeatRequest);
		ResponseEntity<SaveSeatResponse> responseEntity = restTemplate.exchange(
			"/API/V1/seat", HttpMethod.POST, httpEntity,
			new ParameterizedTypeReference<SaveSeatResponse>(){});
		SeatDto seat = responseEntity.getBody().getSeatDto();
		Assert.assertNotEquals( seat,null);
		Assert.assertEquals( saveSeatRequest.getFlightId(),seat.getFlightId());
		Assert.assertEquals( saveSeatRequest.getSeatNumber(),seat.getSeatNumber());
		Assert.assertEquals(saveSeatRequest.getPrice(),seat.getPrice());
	}
	
	@Sql({ "create-db.sql", "insert-data.sql" })
	@Test
	public void testUpdateSeat()
	{
		UpdateSeatRequest updateSeatRequest=new UpdateSeatRequest();
		updateSeatRequest.setId(1L);
		updateSeatRequest.setFlightId(2L);
		updateSeatRequest.setSeatNumber("A6");
		updateSeatRequest.setPrice(new BigDecimal(200));
		HttpEntity<?> httpEntity = new HttpEntity<Object>(updateSeatRequest);
		ResponseEntity<UpdateSeatResponse> responseEntity = restTemplate.exchange(
			"/API/V1/seat", HttpMethod.PUT, httpEntity,
			new ParameterizedTypeReference<UpdateSeatResponse>(){});
		SeatDto seat = responseEntity.getBody().getSeatDto();
		Assert.assertNotEquals(null,seat);
		Assert.assertEquals(updateSeatRequest.getId(),seat.getId());
		Assert.assertEquals( updateSeatRequest.getFlightId(),seat.getFlightId());
		Assert.assertEquals(updateSeatRequest.getSeatNumber(),seat.getSeatNumber());
		Assert.assertEquals(updateSeatRequest.getPrice(),seat.getPrice());
		
	}
	
	@Sql({ "create-db.sql", "insert-data.sql" })
	@Test
	public void testDeleteSeat()
	{
		ResponseEntity<DeleteSeatResponse> responseEntity = restTemplate.exchange(
			"/API/V1/seat/1", HttpMethod.DELETE, null,
			new ParameterizedTypeReference<DeleteSeatResponse>(){});
		Boolean deletePositiveResponse = responseEntity.getBody().getResponse();
		Assert.assertEquals( true,deletePositiveResponse);
		
		ResponseEntity<DeleteSeatResponse> responseEntity2 = restTemplate.exchange(
			"/API/V1/seat/11", HttpMethod.DELETE, null,
			new ParameterizedTypeReference<DeleteSeatResponse>(){});
		Boolean deleteNegativeResponse = responseEntity2.getBody().getResponse();
		Assert.assertEquals(false,deleteNegativeResponse);
		
	}
}
