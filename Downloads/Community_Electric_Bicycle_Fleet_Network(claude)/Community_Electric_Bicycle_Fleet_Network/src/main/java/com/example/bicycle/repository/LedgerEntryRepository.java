package com.example.bicycle.repository;
import com.example.bicycle.model.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {}
