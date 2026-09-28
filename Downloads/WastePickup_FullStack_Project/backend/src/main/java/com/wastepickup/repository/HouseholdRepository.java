package com.wastepickup.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wastepickup.entity.Household;

public interface HouseholdRepository extends JpaRepository<Household,Long>{
	List<Household> findByZone_Id(Long zoneId);
	List<Household> findByReminderFlagTrue();
}