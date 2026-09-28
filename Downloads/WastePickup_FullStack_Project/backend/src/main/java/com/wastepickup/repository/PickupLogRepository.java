package com.wastepickup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wastepickup.entity.PickupLog;

public interface PickupLogRepository extends JpaRepository<PickupLog,Long>{

    @Query("select avg(p.score) from PickupLog p where p.household.id=:id")

    Double average(Long id);

}