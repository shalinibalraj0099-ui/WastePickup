package com.example.bicycle.repository;
import com.example.bicycle.model.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RiderRepository extends JpaRepository<Rider, Long> {}
