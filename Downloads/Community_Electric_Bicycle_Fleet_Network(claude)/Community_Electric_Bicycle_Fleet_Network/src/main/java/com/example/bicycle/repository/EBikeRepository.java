package com.example.bicycle.repository;
import com.example.bicycle.model.EBike;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EBikeRepository extends JpaRepository<EBike, Long> {}
