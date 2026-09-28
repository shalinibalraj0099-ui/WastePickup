package com.example.bicycle.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity @Getter @Setter @NoArgsConstructor
public class LedgerEntry {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private Long accountId; private String description; private double debit;
    private double credit; private LocalDate entryDate = LocalDate.now();
}