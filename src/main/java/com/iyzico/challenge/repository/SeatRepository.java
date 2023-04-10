package com.iyzico.challenge.repository;

import com.iyzico.challenge.model.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<SeatEntity, Long> {
	SeatEntity findSeatById(Long id);
	List<SeatEntity> findByFlightId(Long id);
	List<SeatEntity> findByFlightIdAndCustomerIdIsNull(Long id);
	List<SeatEntity> findAll();
}
