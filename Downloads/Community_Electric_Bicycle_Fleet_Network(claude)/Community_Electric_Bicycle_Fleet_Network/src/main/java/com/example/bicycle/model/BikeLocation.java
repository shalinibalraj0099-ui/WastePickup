package com.example.bicycle.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity @Getter @Setter @NoArgsConstructor
public class BikeLocation {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private Long bikeId; private Double latitude; private Double longitude;
    private LocalDateTime recordedAt = LocalDateTime.now();
}