package com.example.bicycle.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity @Getter @Setter @NoArgsConstructor
public class PurchaseOrder {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private Long vendorId; private String productName; private int quantity;
    private double totalAmount; private String status = "CREATED"; private LocalDate orderDate = LocalDate.now();
}