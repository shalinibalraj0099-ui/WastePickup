package com.wastepickup.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


@Entity
@Table(name = "households")
public class Household {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Address is required")
    private String address;

    @Pattern(regexp = "^\\+[1-9]\\d{7,14}$", message = "Phone number must use international format, for example +14155552671")
    @Column(name = "phone_number")
    private String phoneNumber;


    private boolean reminderFlag = false;

    private boolean reminderSent = false;



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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



    public boolean isReminderFlag() {
        return reminderFlag;
    }


    public void setReminderFlag(boolean reminderFlag) {
        this.reminderFlag = reminderFlag;
    }

    public boolean isReminderSent() {
        return reminderSent;
    }

    public void setReminderSent(boolean reminderSent) {
        this.reminderSent = reminderSent;
    }



    public Zone getZone() {
        return zone;
    }


    public void setZone(Zone zone) {
        this.zone = zone;
    }

}