package com.example.bicycle.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity @Getter @Setter @NoArgsConstructor
public class VendorBill {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private Long purchaseOrderId; private Long vendorId; private double amount;
    private String status = "UNPAID"; private LocalDate billDate = LocalDate.now();
}