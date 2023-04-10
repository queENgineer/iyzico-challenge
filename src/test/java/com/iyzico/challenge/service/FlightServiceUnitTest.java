package com.iyzico.challenge.service;

import com.iyzico.challenge.model.dto.FlightDto;
import com.iyzico.challenge.model.entity.FlightEntity;
import com.iyzico.challenge.model.entity.SeatEntity;
import com.iyzico.challenge.repository.FlightRepository;
import com.iyzico.challenge.repository.SeatRepository;
import com.iyzico.challenge.service.impl.FlightServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@Sql({ "create-db.sql" })
@ExtendWith(MockitoExtension.class)
public class FlightServiceUnitTest {
	@Mock
	FlightRepository flightRepository;
	@Mock
	SeatRepository seatRepository;
	
	@InjectMocks
	FlightServiceImpl flightServiceImpl;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void givenFlight_whenSaveTheFlight_thenReturnTheGivenFlight(){
		
		SaveFlightRequest saveFlightRequest=new SaveFlightRequest();
		saveFlightRequest.setFlightName("TK-1111");
		saveFlightRequest.setDescription("From IST to JFK");
		String txnId = UUID.randomUUID().toString();
		
		FlightEntity flightEntityToBeSaved=new FlightEntity();
		flightEntityToBeSaved.setFlightName("TK-1111");
		flightEntityToBeSaved.setDescription("From IST to JFK");
		
		FlightEntity savedFlightEntity=new FlightEntity();
		savedFlightEntity.setId(1L);
		savedFlightEntity.setFlightName("TK-1111");
		savedFlightEntity.setDescription("From IST to JFK");
		
		when(flightRepository.save(flightEntityToBeSaved)).thenReturn(savedFlightEntity);
		FlightDto flightDto=flightServiceImpl.saveFlight(txnId,saveFlightRequest);
		
		assertNotEquals(null,flightDto);
		assertEquals( savedFlightEntity.getId(),flightDto.getId());
		assertEquals( saveFlightRequest.getFlightName(),flightDto.getFlightName());
		assertEquals(saveFlightRequest.getDescription(),flightDto.getDescription());
		

	}
	
	@Test
	public void givenFlightId_IfTheFlightFoundById_thenDeleteTheFlightAndReturnTrue_IfNot_thenReturnFalse(){
		FlightEntity flightEntity=new FlightEntity();
		flightEntity.setId(1L);
		flightEntity.setFlightName("TK-1111");
		flightEntity.setDescription("From IST to JFK");
		
		List<SeatEntity> seatEntityList=new ArrayList<>();
		SeatEntity seatEntity=new SeatEntity();
		seatEntity.setId(1L);
		seatEntity.setFlightId(1L);
		seatEntity.setSeatNumber("A11");
		seatEntity.setPrice(new BigDecimal(100));
		seatEntityList.add(seatEntity);
		
		String txnId = UUID.randomUUID().toString();
		
		when(seatRepository.findByFlightId(flightEntity.getId())).thenReturn(seatEntityList);
		when(flightRepository.findFlightById(flightEntity.getId())).thenReturn(flightEntity);
		Boolean deletePositiveResponse=flightServiceImpl.deleteFlightById(txnId,flightEntity.getId());
		assertEquals(true,deletePositiveResponse);
		
		txnId = UUID.randomUUID().toString();
		
		when(flightRepository.findFlightById(15L)).thenReturn(null);
		Boolean deleteNegativeResponse=flightServiceImpl.deleteFlightById(txnId,15L);
		assertEquals(false,deleteNegativeResponse);
		
	}
	
	@Test
	public void givenFlight_whenUpdateTheFlightName_thenReturnTheUpdatedFlight(){
		
		UpdateFlightRequest updateFlightRequest=new UpdateFlightRequest();
		updateFlightRequest.setId(1L);
		updateFlightRequest.setFlightName("TK-5577");
		String txnId = UUID.randomUUID().toString();
		
		FlightEntity flightTobeUpdated=new FlightEntity();
		flightTobeUpdated.setId(1L);
		flightTobeUpdated.setFlightName("TK-1111");
		flightTobeUpdated.setDescription("From IST to JFK");
		
		FlightEntity updatedFlight=new FlightEntity();
		updatedFlight.setId(1L);
		updatedFlight.setFlightName("TK-5577");
		updatedFlight.setDescription("From IST to JFK");
		
		
		when(flightRepository.findFlightById(updateFlightRequest.getId())).thenReturn(flightTobeUpdated);
		when(flightRepository.save(updatedFlight)).thenReturn(updatedFlight);
		FlightDto flightDto=flightServiceImpl.updateFlight(txnId,updateFlightRequest);
		
		
		assertNotEquals(null,flightDto);
		assertEquals(updateFlightRequest.getId(), flightDto.getId());
		assertEquals( updateFlightRequest.getFlightName(),flightDto.getFlightName());
		assertEquals(flightTobeUpdated.getDescription(),flightDto.getDescription());
	}
	
	
	@Test
	public void givenFlight_whenUpdateTheFlightDescription_thenReturnTheUpdatedFlight(){
		
		UpdateFlightRequest updateFlightRequest=new UpdateFlightRequest();
		updateFlightRequest.setId(1L);
		updateFlightRequest.setDescription("From ANK to IST");
		String txnId = UUID.randomUUID().toString();
		
		FlightEntity flightTobeUpdated=new FlightEntity();
		flightTobeUpdated.setId(1L);
		flightTobeUpdated.setFlightName("TK-1111");
		flightTobeUpdated.setDescription("From IST to JFK");
		
		
		FlightEntity updatedFlight=new FlightEntity();
		updatedFlight.setId(1L);
		updatedFlight.setFlightName("TK-1111");
		updatedFlight.setDescription("From ANK to IST");

		
		
		when(flightRepository.findFlightById(updateFlightRequest.getId())).thenReturn(flightTobeUpdated);
		when(flightRepository.save(updatedFlight)).thenReturn(updatedFlight);
		FlightDto flightDto=flightServiceImpl.updateFlight(txnId,updateFlightRequest);
		
		
		assertNotEquals(null,flightDto);
		assertEquals(updateFlightRequest.getId(), flightDto.getId());
		assertEquals(flightTobeUpdated.getFlightName(),flightDto.getFlightName());
		assertEquals(updateFlightRequest.getDescription(),flightDto.getDescription());
		
	}
	
	
	
}
