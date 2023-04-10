package com.iyzico.challenge.repository;

import com.iyzico.challenge.model.entity.FlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<FlightEntity, Long> {
	FlightEntity findFlightById(Long id);
	List<FlightEntity> findAll();
}
