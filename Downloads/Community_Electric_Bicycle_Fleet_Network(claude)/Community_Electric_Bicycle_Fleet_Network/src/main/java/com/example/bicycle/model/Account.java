package com.example.bicycle.model;

import jakarta.persistence.*;
import lombok.*;


@Entity @Getter @Setter @NoArgsConstructor
public class Account {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(unique=true) private String code; private String name; private String category;
    private double balance;
}