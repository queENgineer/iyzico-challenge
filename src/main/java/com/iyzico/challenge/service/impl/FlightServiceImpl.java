package com.iyzico.challenge.service.impl;

import com.iyzico.challenge.model.dto.FlightDto;
import com.iyzico.challenge.model.dto.SeatDto;
import com.iyzico.challenge.model.entity.FlightEntity;
import com.iyzico.challenge.model.entity.SeatEntity;
import com.iyzico.challenge.service.SaveFlightRequest;
import com.iyzico.challenge.service.UpdateFlightRequest;
import com.iyzico.challenge.repository.FlightRepository;
import com.iyzico.challenge.repository.SeatRepository;
import com.iyzico.challenge.service.FlightService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {
	
	@Autowired
	private FlightRepository flightRepository;
	
	@Autowired
	private SeatRepository seatRepository;
	
	@Override
	public FlightDto saveFlight(String txnId, SaveFlightRequest request) {
	
			log.info("{} FlightServiceImpl - saveFlight() start. ", txnId);
			
			FlightDto flightDto = new FlightDto();
			flightDto.setFlightName(request.getFlightName());
			flightDto.setDescription(request.getDescription());
			
			ModelMapper modelMapper = new ModelMapper();
			FlightEntity entity = flightRepository.save(modelMapper.map(flightDto, FlightEntity.class));
			
			log.info("{} FlightServiceImpl - saveFlight() end. ", txnId);
	
			return modelMapper.map(entity, FlightDto.class);
		
	}
	
	@Override
	public Boolean deleteFlightById(String txnId, Long id) {
		log.info("{} FlightServiceImpl - deleteFlightById() start.", txnId);
		ModelMapper modelMapper = new ModelMapper();
			FlightEntity flightEntityToDelete = flightRepository.findFlightById(id);
			
			if (flightEntityToDelete != null) {
				flightRepository.deleteById(flightEntityToDelete.getId());
			    List<SeatEntity> seatEntityList=seatRepository.findByFlightId(id);
				for(SeatEntity seatEntity:seatEntityList){
					seatRepository.deleteById(seatEntity.getId());
				}
				log.info("{} FlightServiceImpl - deleteFlightById() end.", txnId);
				return true;
			} else {
				log.error("{} FlightServiceImpl - deleteFlightById() - Flight Could not be found.", txnId);
				return false;
			}
	}
	
	@Override
	public FlightDto updateFlight(String txnId, UpdateFlightRequest request) {
		log.info("{} FlightServiceImpl - updateFlight() start. ", txnId);
		FlightEntity flightEntityToUpdate =flightRepository.findFlightById(request.getId());
		if(flightEntityToUpdate !=null){
			
			flightEntityToUpdate.setFlightName(!ObjectUtils.isEmpty(request.getFlightName())?request.getFlightName():flightEntityToUpdate.getFlightName());
			flightEntityToUpdate.setDescription(!ObjectUtils.isEmpty(request.getDescription())?request.getDescription():flightEntityToUpdate.getDescription());
			ModelMapper modelMapper=new ModelMapper();
			FlightEntity entity=flightRepository.save(flightEntityToUpdate);
			log.info("{} FlightServiceImpl - updateFlight() end. ", txnId);
			
			return modelMapper.map(entity,FlightDto.class);
		}else{
			log.error("{} FlightServiceImpl - updateFlight() - Could not find the flight with id:{} ", txnId,request.getId());
			return null;
		}
		
	}
	
	@Override
	public List<FlightDto> getFlightList(String txnId) {
		log.info("{} FlightServiceImpl - listFlights() started. ", txnId);
		
		List<FlightDto>  flightDtoList = new ArrayList<>();
		ModelMapper modelMapper=new ModelMapper();
		
			Iterable<FlightEntity> iterable = flightRepository.findAll();
			if (iterable != null) {
				for (FlightEntity entity : iterable) {
					FlightDto flightDto=modelMapper.map(entity,FlightDto.class);
					
					List<SeatDto> seatDtoList=seatRepository.findByFlightIdAndCustomerIdIsNull(entity.getId()).stream()
						.map(seat -> modelMapper.map(seat, SeatDto.class))
						.collect(Collectors.toList());
					flightDto.setSeatDtoList(seatDtoList);
					flightDtoList.add(flightDto);
				}
			}
			
			log.info("{} FlightServiceImpl - listFlights() end. ", txnId);
			return flightDtoList;
	
	}
	
}
