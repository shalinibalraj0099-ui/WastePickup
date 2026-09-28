package com.example.bicycle.model;

import jakarta.persistence.*;
import lombok.*;


@Entity @Getter @Setter @NoArgsConstructor
public class AnalyticAccount {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private String name; private String description;
}