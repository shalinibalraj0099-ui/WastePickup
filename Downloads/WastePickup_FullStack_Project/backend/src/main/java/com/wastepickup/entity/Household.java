package com.wastepickup.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name = "households")
public class Household {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Address is required")
    private String address;


    private boolean reminderFlag = false;



    @ManyToOne
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;



    // Default constructor
    public Household() {
    }



    // Getters and Setters


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }



    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }



    public boolean isReminderFlag() {
        return reminderFlag;
    }


    public void setReminderFlag(boolean reminderFlag) {
        this.reminderFlag = reminderFlag;
    }



    public Zone getZone() {
        return zone;
    }


    public void setZone(Zone zone) {
        this.zone = zone;
    }

}