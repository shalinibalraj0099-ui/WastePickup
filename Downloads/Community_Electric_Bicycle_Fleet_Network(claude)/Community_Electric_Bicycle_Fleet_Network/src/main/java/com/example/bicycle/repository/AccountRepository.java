package com.example.bicycle.repository;
import com.example.bicycle.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AccountRepository extends JpaRepository<Account, Long> {}
