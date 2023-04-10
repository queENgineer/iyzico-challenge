package com.iyzico.challenge.service.impl;

import com.iyzico.challenge.model.dto.SeatDto;
import com.iyzico.challenge.model.entity.FlightEntity;
import com.iyzico.challenge.model.entity.SeatEntity;
import com.iyzico.challenge.service.SaveSeatRequest;
import com.iyzico.challenge.service.UpdateSeatRequest;
import com.iyzico.challenge.repository.FlightRepository;
import com.iyzico.challenge.repository.SeatRepository;
import com.iyzico.challenge.service.SeatService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.InvalidParameterException;

@Service
@Slf4j
@AllArgsConstructor
public class SeatServiceImpl implements SeatService {
	
	@Autowired
	private SeatRepository seatRepository;
	@Autowired
	private FlightRepository flightRepository;
	
	@Override
	public SeatDto saveSeat(String txnId, SaveSeatRequest request) {
		log.info("{} SeatServiceImpl - saveSeat() start. ", txnId);
		FlightEntity flightEntity=flightRepository.findFlightById(request.getFlightId());
		if(!ObjectUtils.isEmpty(flightEntity)) {
			SeatDto seatDto = new SeatDto();
			seatDto.setSeatNumber(request.getSeatNumber());
			seatDto.setFlightId(request.getFlightId());
			seatDto.setPrice(request.getPrice());
			
			
			ModelMapper modelMapper = new ModelMapper();
			SeatEntity entity = seatRepository.save(modelMapper.map(seatDto, SeatEntity.class));
			
			log.info("{} SeatServiceImpl - saveSeat() end. ", txnId);
			
			return modelMapper.map(entity, SeatDto.class);
		}else{
			log.error("{} SeatServiceImpl - saveSeat() - Could not find the flight with id:{} ", txnId,request.getFlightId());
			throw new InvalidParameterException();
		}
	}
	
	@Override
	public Boolean deleteSeatById(String txnId, Long id) {
		log.info("{} SeatServiceImpl - deleteSeatById() start.", txnId);

			SeatEntity seatToDelete = seatRepository.findSeatById(id);
			
			if (seatToDelete != null) {
				seatRepository.deleteById(seatToDelete.getId());
				log.info("{} SeatServiceImpl - deleteSeatById() end.", txnId);
				return true;
			} else {
				log.error("{} SeatServiceImpl - deleteSeatById() - Seat Could not be found.", txnId);
				return false;
			}
	}
	
	@Override
	public SeatDto updateSeat(String txnId, UpdateSeatRequest request) {
		log.info("{} SeatServiceImpl - updateSeat() start. ", txnId);

		SeatEntity seatToUpdate=seatRepository.findSeatById(request.getId());
		if(seatToUpdate!=null){
			FlightEntity flightEntity=flightRepository.findFlightById(seatToUpdate.getFlightId());
			if(!ObjectUtils.isEmpty(flightEntity)) {
			
			seatToUpdate.setSeatNumber(!ObjectUtils.isEmpty(request.getSeatNumber())?request.getSeatNumber():seatToUpdate.getSeatNumber());
			seatToUpdate.setFlightId(!ObjectUtils.isEmpty(request.getFlightId())?request.getFlightId():seatToUpdate.getFlightId());
			seatToUpdate.setPrice(!ObjectUtils.isEmpty(request.getPrice())?request.getPrice():seatToUpdate.getPrice());
			
			ModelMapper modelMapper=new ModelMapper();
			SeatEntity entity=seatRepository.save(seatToUpdate);
			log.info("{} SeatServiceImpl - updateSeat() end. ", txnId);
			
			return modelMapper.map(entity,SeatDto.class);
			}else{
				log.error("{} SeatServiceImpl - updateSeat() - Could not find the flight with id:{} ", txnId,request.getFlightId());
				throw new InvalidParameterException();
			}
		}else{
			log.error("{} SeatServiceImpl - updateSeat() - Could not find the seat with id:{} ", txnId,request.getId());
			return null;
		}
		
	}
}
