package com.iyzico.challenge.model.entity;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;


@Entity
@Data
@Table(schema = "PUBLIC",name = "PAYMENT")
public class Payment {
    
    @Basic
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    @Column(name="price")
    private BigDecimal price;
    
    @Column(name="bank_response")
    private String bankResponse;

 }
