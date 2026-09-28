package com.example.bicycle.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity @Getter @Setter @NoArgsConstructor
public class Payment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private String paymentType; private String referenceType; private Long referenceId;
    private double amount; private String method; private LocalDate paymentDate = LocalDate.now();
}