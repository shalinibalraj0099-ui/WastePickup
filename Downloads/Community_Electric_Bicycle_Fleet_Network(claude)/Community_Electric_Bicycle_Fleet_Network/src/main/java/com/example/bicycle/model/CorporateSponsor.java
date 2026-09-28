package com.example.bicycle.model;

import jakarta.persistence.*;
import lombok.*;


@Entity @Getter @Setter @NoArgsConstructor
public class CorporateSponsor {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private String companyName; private String contactName; private String email;
}