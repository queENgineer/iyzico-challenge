package com.iyzico.challenge.service;

import com.iyzico.challenge.model.dto.SeatDto;
import com.iyzico.challenge.model.entity.FlightEntity;
import com.iyzico.challenge.model.entity.SeatEntity;
import com.iyzico.challenge.repository.FlightRepository;
import com.iyzico.challenge.repository.SeatRepository;
import com.iyzico.challenge.service.impl.SeatServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SeatServiceUnitTest {
	@Mock
	SeatRepository seatRepository;
	@Mock
	FlightRepository flightRepository;
	
	@InjectMocks
	SeatServiceImpl seatServiceImpl;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void givenSeat_whenSaveTheSeatWithExistingFlightId_thenReturnTheGivenSeat(){
		
		SaveSeatRequest saveSeatRequest=new SaveSeatRequest();
		saveSeatRequest.setPrice(new BigDecimal(100.0));
		saveSeatRequest.setFlightId(1L);
		saveSeatRequest.setSeatNumber("A11");
		String txnId = UUID.randomUUID().toString();
		
		SeatEntity seatEntityToBeSaved=new SeatEntity();
		seatEntityToBeSaved.setPrice(new BigDecimal(100.0));
		seatEntityToBeSaved.setFlightId(1L);
		seatEntityToBeSaved.setSeatNumber("A11");
		
		SeatEntity savedSeatEntity=new SeatEntity();
		savedSeatEntity.setId(2L);
		savedSeatEntity.setPrice(new BigDecimal(100.0));
		savedSeatEntity.setFlightId(1L);
		savedSeatEntity.setSeatNumber("A11");
		
		FlightEntity flightEntity=new FlightEntity();
		flightEntity.setId(1L);
		flightEntity.setFlightName("TK-1111");
		flightEntity.setDescription("From IST to JFK");
		
		when(seatRepository.save(seatEntityToBeSaved)).thenReturn(savedSeatEntity);
		when(flightRepository.findFlightById(saveSeatRequest.getFlightId())).thenReturn(flightEntity);
		SeatDto seatDto=seatServiceImpl.saveSeat(txnId,saveSeatRequest);
		
		assertNotEquals(null, seatDto);
		assertEquals(savedSeatEntity.getId(),seatDto.getId());
		assertEquals(saveSeatRequest.getFlightId(),seatDto.getFlightId());
		assertEquals(saveSeatRequest.getSeatNumber(),seatDto.getSeatNumber());
		assertEquals(saveSeatRequest.getPrice(),seatDto.getPrice());
	}
	
	@Test
	public void givenSeat_whenSaveTheSeatWithNotExistingFlightId_thenThrowsInvalidParameterException(){
		
		SaveSeatRequest saveSeatRequest=new SaveSeatRequest();
		saveSeatRequest.setPrice(new BigDecimal(100.0));
		saveSeatRequest.setFlightId(1L);
		saveSeatRequest.setSeatNumber("A11");
		String txnId = UUID.randomUUID().toString();
		
		SeatEntity seatEntityTobeSaved=new SeatEntity();
		seatEntityTobeSaved.setPrice(new BigDecimal(100.0));
		seatEntityTobeSaved.setFlightId(1L);
		seatEntityTobeSaved.setSeatNumber("A11");
		
		SeatEntity savedSeatEntity=new SeatEntity();
		savedSeatEntity.setId(2L);
		savedSeatEntity.setPrice(new BigDecimal(100.0));
		savedSeatEntity.setFlightId(1L);
		savedSeatEntity.setSeatNumber("A11");
		
		
		when(flightRepository.findFlightById(saveSeatRequest.getFlightId())).thenReturn(null);
		when(seatRepository.save(seatEntityTobeSaved)).thenReturn(savedSeatEntity);
		
		assertThrows(Exception.class,()->seatServiceImpl.saveSeat(txnId,saveSeatRequest));
		
	}
	@Test
	public void givenSeatId_IfTheSeatFoundById_thenDeleteTheSeatAndReturnTrue_IfNot_thenReturnFalse(){
		SeatEntity seatEntity=new SeatEntity();
		seatEntity.setId(1L);
		seatEntity.setFlightId(1L);
		seatEntity.setSeatNumber("A11");
		seatEntity.setPrice(new BigDecimal(100));
		
		String txnId = UUID.randomUUID().toString();
		when(seatRepository.findSeatById(seatEntity.getId())).thenReturn(seatEntity);
		Boolean deletePositiveResponse=seatServiceImpl.deleteSeatById(txnId,seatEntity.getId());
		assertEquals(true,deletePositiveResponse);
		
		txnId = UUID.randomUUID().toString();
		
		when(flightRepository.findFlightById(5L)).thenReturn(null);
		Boolean deleteNegativeResponse=seatServiceImpl.deleteSeatById(txnId,5L);
		assertEquals(false,deleteNegativeResponse);
		
	}
	
	@Test
	public void givenSeat_whenUpdateTheSeatNumber_thenReturnTheUpdatedSeat(){
		
		UpdateSeatRequest updateSeatRequest=new UpdateSeatRequest();
		updateSeatRequest.setId(1L);
		updateSeatRequest.setSeatNumber("A11");
		String txnId = UUID.randomUUID().toString();
		
		SeatEntity seatTobeUpdated=new SeatEntity();
		seatTobeUpdated.setId(1L);
		seatTobeUpdated.setFlightId(1L);
		seatTobeUpdated.setSeatNumber("A1");
		seatTobeUpdated.setPrice(new BigDecimal(100));
		
		
		SeatEntity updatedSeat=new SeatEntity();
		updatedSeat.setId(1L);
		updatedSeat.setFlightId(1L);
		updatedSeat.setSeatNumber("A11");
		updatedSeat.setPrice(new BigDecimal(100));
		
		FlightEntity flightEntity=new FlightEntity();
		flightEntity.setId(1L);
		flightEntity.setFlightName("TK-1111");
		flightEntity.setDescription("From IST to JFK");
		
		when(seatRepository.findSeatById(updateSeatRequest.getId())).thenReturn(seatTobeUpdated);
		when(flightRepository.findFlightById(seatTobeUpdated.getFlightId())).thenReturn(flightEntity);
		when(seatRepository.save(updatedSeat)).thenReturn(updatedSeat);
		SeatDto seatDto=seatServiceImpl.updateSeat(txnId,updateSeatRequest);
		
		
		assertNotEquals(null,seatDto);
		assertEquals(updateSeatRequest.getId(), seatDto.getId());
		assertEquals(updateSeatRequest.getSeatNumber(), seatDto.getSeatNumber());
		assertEquals( seatTobeUpdated.getFlightId(),seatDto.getFlightId());
		assertEquals( seatTobeUpdated.getPrice(),seatDto.getPrice());
		
	}
	
	@Test
	public void givenSeat_whenUpdateTheFlightId_thenReturnTheUpdatedSeat(){
		
		UpdateSeatRequest updateSeatRequest=new UpdateSeatRequest();
		updateSeatRequest.setId(1L);
		updateSeatRequest.setFlightId(2L);
		String txnId = UUID.randomUUID().toString();
		
		SeatEntity seatTobeUpdated=new SeatEntity();
		seatTobeUpdated.setId(1L);
		seatTobeUpdated.setFlightId(1L);
		seatTobeUpdated.setSeatNumber("A1");
		seatTobeUpdated.setPrice(new BigDecimal(100));
		
		
		SeatEntity updatedSeat=new SeatEntity();
		updatedSeat.setId(1L);
		updatedSeat.setFlightId(2L);
		updatedSeat.setSeatNumber("A1");
		updatedSeat.setPrice(new BigDecimal(100));
		
		FlightEntity flightEntity=new FlightEntity();
		flightEntity.setId(1L);
		flightEntity.setFlightName("TK-1111");
		flightEntity.setDescription("From IST to JFK");
		
		when(seatRepository.findSeatById(updateSeatRequest.getId())).thenReturn(seatTobeUpdated);
		when(flightRepository.findFlightById(seatTobeUpdated.getFlightId())).thenReturn(flightEntity);
		when(seatRepository.save(updatedSeat)).thenReturn(updatedSeat);
		SeatDto seatDto=seatServiceImpl.updateSeat(txnId,updateSeatRequest);
		
		
		assertNotEquals(null,seatDto);
		assertEquals(updateSeatRequest.getId(), seatDto.getId());
		assertEquals(updateSeatRequest.getFlightId(), seatDto.getFlightId());
		assertEquals(seatTobeUpdated.getSeatNumber(), seatDto.getSeatNumber());
		assertEquals(seatTobeUpdated.getPrice(),seatDto.getPrice());
		
	}
	
	@Test
	public void givenSeat_whenUpdateThePrice_thenReturnTheUpdatedSeat(){
		
		UpdateSeatRequest updateSeatRequest=new UpdateSeatRequest();
		updateSeatRequest.setId(1L);
		updateSeatRequest.setPrice(new BigDecimal(200));
		String txnId = UUID.randomUUID().toString();
		
		SeatEntity seatTobeUpdated=new SeatEntity();
		seatTobeUpdated.setId(1L);
		seatTobeUpdated.setFlightId(1L);
		seatTobeUpdated.setSeatNumber("A1");
		seatTobeUpdated.setPrice(new BigDecimal(100));
		
		
		SeatEntity updatedSeat=new SeatEntity();
		updatedSeat.setId(1L);
		updatedSeat.setFlightId(1L);
		updatedSeat.setSeatNumber("A1");
		updatedSeat.setPrice(new BigDecimal(200));
		
		FlightEntity flightEntity=new FlightEntity();
		flightEntity.setId(1L);
		flightEntity.setFlightName("TK-1111");
		flightEntity.setDescription("From IST to JFK");
		
		when(seatRepository.findSeatById(updateSeatRequest.getId())).thenReturn(seatTobeUpdated);
		when(flightRepository.findFlightById(seatTobeUpdated.getFlightId())).thenReturn(flightEntity);
		when(seatRepository.save(updatedSeat)).thenReturn(updatedSeat);
		SeatDto seatDto=seatServiceImpl.updateSeat(txnId,updateSeatRequest);
		
		
		assertNotEquals(null,seatDto);
		assertEquals(updateSeatRequest.getId(), seatDto.getId());
		assertEquals(updateSeatRequest.getPrice(),seatDto.getPrice());
		assertEquals(seatTobeUpdated.getFlightId(), seatDto.getFlightId());
		assertEquals(seatTobeUpdated.getSeatNumber(), seatDto.getSeatNumber());
		
	}
}