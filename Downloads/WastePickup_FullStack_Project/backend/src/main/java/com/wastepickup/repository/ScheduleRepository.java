package com.wastepickup.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.wastepickup.entity.Schedule;
public interface ScheduleRepository extends JpaRepository<Schedule,Long>{}