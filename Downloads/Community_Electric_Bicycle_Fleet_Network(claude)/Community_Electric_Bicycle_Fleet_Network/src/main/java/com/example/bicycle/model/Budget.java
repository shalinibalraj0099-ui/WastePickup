package com.example.bicycle.model;

import jakarta.persistence.*;
import lombok.*;


@Entity @Getter @Setter @NoArgsConstructor
public class Budget {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private Long analyticAccountId; private double allocatedAmount; private double actualAmount;
    private String period;
}