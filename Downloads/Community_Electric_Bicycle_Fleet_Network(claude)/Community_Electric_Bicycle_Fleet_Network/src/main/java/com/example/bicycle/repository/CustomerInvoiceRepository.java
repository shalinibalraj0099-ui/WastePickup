package com.example.bicycle.repository;
import com.example.bicycle.model.CustomerInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CustomerInvoiceRepository extends JpaRepository<CustomerInvoice, Long> {}
