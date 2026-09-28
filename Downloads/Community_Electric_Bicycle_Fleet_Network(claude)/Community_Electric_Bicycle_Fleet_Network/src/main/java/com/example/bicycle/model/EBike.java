package com.example.bicycle.model;

import jakarta.persistence.*;
import lombok.*;


@Entity @Getter @Setter @NoArgsConstructor
public class EBike {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(unique=true) private String bikeCode;
    private int batteryPercent = 100;
    private String status = "AVAILABLE";
    private Double latitude; private Double longitude;
}