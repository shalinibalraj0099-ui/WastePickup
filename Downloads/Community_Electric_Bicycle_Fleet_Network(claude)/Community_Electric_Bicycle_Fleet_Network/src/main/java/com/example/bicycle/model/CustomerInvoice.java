package com.example.bicycle.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity @Getter @Setter @NoArgsConstructor
public class CustomerInvoice {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private Long salesOrderId; private Long sponsorId; private double amount;
    private String status = "UNPAID"; private LocalDate invoiceDate = LocalDate.now();
}