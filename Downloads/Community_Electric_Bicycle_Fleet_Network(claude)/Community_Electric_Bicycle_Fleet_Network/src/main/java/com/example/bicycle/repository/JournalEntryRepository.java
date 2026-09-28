package com.example.bicycle.repository;
import com.example.bicycle.model.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {}
