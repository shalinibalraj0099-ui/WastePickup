package com.example.bicycle.repository;
import com.example.bicycle.model.BikeLocation;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BikeLocationRepository extends JpaRepository<BikeLocation, Long> {}
