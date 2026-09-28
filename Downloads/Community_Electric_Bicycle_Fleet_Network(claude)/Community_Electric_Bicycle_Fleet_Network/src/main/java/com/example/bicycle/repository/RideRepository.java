package com.example.bicycle.repository;
import com.example.bicycle.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RideRepository extends JpaRepository<Ride, Long> {}
