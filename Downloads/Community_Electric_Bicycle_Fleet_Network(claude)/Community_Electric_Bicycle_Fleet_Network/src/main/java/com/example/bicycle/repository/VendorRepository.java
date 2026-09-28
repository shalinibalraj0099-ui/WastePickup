package com.example.bicycle.repository;
import com.example.bicycle.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
public interface VendorRepository extends JpaRepository<Vendor, Long> {}
