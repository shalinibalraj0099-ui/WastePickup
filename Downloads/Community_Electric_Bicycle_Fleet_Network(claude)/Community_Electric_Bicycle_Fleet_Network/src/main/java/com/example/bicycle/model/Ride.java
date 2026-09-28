package com.example.bicycle.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity @Getter @Setter @NoArgsConstructor
public class Ride {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private Long riderId; private Long bikeId;
    private LocalDateTime startTime; private LocalDateTime endTime;
    private long durationMinutes; private double fare; private String status = "ACTIVE";
}