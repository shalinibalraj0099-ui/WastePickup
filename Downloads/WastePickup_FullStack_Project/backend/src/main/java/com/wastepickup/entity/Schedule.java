package com.wastepickup.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "schedules")
public class Schedule {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull(message = "Pickup date is required")
    private LocalDate pickupDate;


    @NotNull(message = "Start time is required")
    private LocalTime startTime;


    @NotNull(message = "End time is required")
    private LocalTime endTime;



    @ManyToOne
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;



    // Constructors

    public Schedule() {
    }


    public Schedule(LocalDate pickupDate,
                    LocalTime startTime,
                    LocalTime endTime,
                    Zone zone) {

        this.pickupDate = pickupDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.zone = zone;
    }



    // Getters and Setters


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public LocalDate getPickupDate() {
        return pickupDate;
    }


    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }


    public LocalTime getStartTime() {
        return startTime;
    }


    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }


    public LocalTime getEndTime() {
        return endTime;
    }


    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }


    public Zone getZone() {
        return zone;
    }


    public void setZone(Zone zone) {
        this.zone = zone;
    }
}