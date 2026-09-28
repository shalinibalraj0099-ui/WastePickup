package com.wastepickup.entity;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
@Entity
public class PickupLog{
@Id @GeneratedValue
private Long id;
private Integer score;
private LocalDateTime pickupTime;
@ManyToOne
private Household household;
public Long getId(){return id;}
public Integer getScore(){return score;}
public void setScore(Integer s){score=s;}
public LocalDateTime getPickupTime(){return pickupTime;}
public void setPickupTime(LocalDateTime pickupTime){this.pickupTime=pickupTime;}
public Household getHousehold(){return household;}
public void setHousehold(Household household){this.household=household;}
}