package com.example.bicycle.repository;
import com.example.bicycle.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {}
