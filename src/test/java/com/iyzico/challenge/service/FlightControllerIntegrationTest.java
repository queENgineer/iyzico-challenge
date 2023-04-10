package com.iyzico.challenge.service;

import com.iyzico.challenge.model.dto.FlightDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class FlightControllerIntegrationTest
{
	@Autowired
	private TestRestTemplate restTemplate;
	
	
	@Sql({ "create-db.sql", "insert-data.sql" })
	@Test
	public void testSaveFlight()
	{
		SaveFlightRequest saveFlightRequest=new SaveFlightRequest();
		saveFlightRequest.setFlightName("TK-1111");
		saveFlightRequest.setDescription("From IST to JFK");
		HttpEntity<?> httpEntity = new HttpEntity<Object>(saveFlightRequest);
		ResponseEntity<SaveFlightResponse> responseEntity = restTemplate.exchange(
			"/API/V1/flight", HttpMethod.POST, httpEntity,
			new ParameterizedTypeReference<SaveFlightResponse>(){});
		FlightDto flight = responseEntity.getBody().getFlightDto();
		
		Assert.assertNotEquals(null,flight);
		Assert.assertEquals(saveFlightRequest.getFlightName(),flight.getFlightName());
		Assert.assertEquals( saveFlightRequest.getDescription(),flight.getDescription());
	}


	@Sql({ "create-db.sql", "insert-data.sql" })
	@Test
	public void testUpdateFlight()
	{
		UpdateFlightRequest updateFlightRequest=new UpdateFlightRequest();
		updateFlightRequest.setId(2L);
		updateFlightRequest.setFlightName("TK-1155");
		updateFlightRequest.setDescription("From IST5 to JFK5");
		HttpEntity<?> httpEntity = new HttpEntity<Object>(updateFlightRequest);
		ResponseEntity<UpdateFlightResponse> responseEntity = restTemplate.exchange(
			"/API/V1/flight", HttpMethod.PUT, httpEntity,
			new ParameterizedTypeReference<UpdateFlightResponse>(){});
		FlightDto flight = responseEntity.getBody().getFlightDto();
		
		Assert.assertNotEquals(null,flight);
		Assert.assertEquals(updateFlightRequest.getId(),flight.getId());
		Assert.assertEquals(updateFlightRequest.getFlightName(),flight.getFlightName());
		Assert.assertEquals(updateFlightRequest.getDescription(),flight.getDescription());
	}
	
	@Sql({ "create-db.sql", "insert-data.sql" })
	@Test
	public void testDeleteFlight()
	{
		
		ResponseEntity<DeleteFlightResponse> responseEntity = restTemplate.exchange(
			"/API/V1/flight/1", HttpMethod.DELETE, null,
			new ParameterizedTypeReference<DeleteFlightResponse>(){});
		Boolean deletePositiveResponse = responseEntity.getBody().getResponse();
		Assert.assertEquals( true,deletePositiveResponse);
		
		
		ResponseEntity<DeleteFlightResponse> responseEntity2 = restTemplate.exchange(
			"/API/V1/flight/11", HttpMethod.DELETE, null,
			new ParameterizedTypeReference<DeleteFlightResponse>(){});
		Boolean deleteNegativeResponse = responseEntity2.getBody().getResponse();
		Assert.assertEquals( false,deleteNegativeResponse);
	}
	
	
	@Sql({ "create-db.sql", "insert-data.sql" })
	@Test
	public void testGetFlightList()
	{
		ResponseEntity<GetFlightListResponse> responseEntity = restTemplate.exchange(
			"/API/V1/flightList", HttpMethod.GET, null,
			new ParameterizedTypeReference<GetFlightListResponse>(){});
		List<FlightDto> list = responseEntity.getBody().getFlightList();
		Assert.assertEquals( 3,list.size());
		Assert.assertEquals( 3,list.get(0).getSeatDtoList().size());
		
	}
}