package com.wastepickup.entity;
import jakarta.persistence.*;
@Entity
public class Zone{
@Id @GeneratedValue
private Long id;
private String name;
public Long getId(){return id;}
public String getName(){return name;}
public void setName(String n){name=n;}
}