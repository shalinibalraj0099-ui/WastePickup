package com.example.bicycle.repository;
import com.example.bicycle.model.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
public interface JournalRepository extends JpaRepository<Journal, Long> {}
