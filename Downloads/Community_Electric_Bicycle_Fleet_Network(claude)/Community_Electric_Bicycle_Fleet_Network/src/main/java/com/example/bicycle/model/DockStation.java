package com.example.bicycle.model;

import jakarta.persistence.*;
import lombok.*;


@Entity @Getter @Setter @NoArgsConstructor
public class DockStation {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private String name; private String location; private int totalSlots;
    private int availableSlots; private int chargingSlots;
}