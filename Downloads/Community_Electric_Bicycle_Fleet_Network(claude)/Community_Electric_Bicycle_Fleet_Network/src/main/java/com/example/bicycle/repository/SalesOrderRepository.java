package com.example.bicycle.repository;
import com.example.bicycle.model.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {}
