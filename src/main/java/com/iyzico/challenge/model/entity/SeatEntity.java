package com.iyzico.challenge.model.entity;


import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Data
@Table( schema = "PUBLIC",name = "SEAT")
public class SeatEntity {
	
	@Basic
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="flight_id")
	private Long flightId;
	
	@Column(name="seat_number")
	private String seatNumber;
	
	
	@Column(name="price")
	private BigDecimal price;
	
	@Column(name="customer_id")
	private Long customerId;
	
}