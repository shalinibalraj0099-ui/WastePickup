package com.wastepickup.Services;

import java.util.List;

import com.wastepickup.entity.Schedule;


public interface ScheduleService {


    // Create a new pickup schedule
    Schedule createSchedule(Schedule schedule);



    // Get all schedules
    List<Schedule> getAllSchedules();



    // Get schedule by ID
    Schedule getScheduleById(Long id);



    // Update existing schedule
    Schedule updateSchedule(Long id, Schedule schedule);



    // Delete schedule
    void deleteSchedule(Long id);



}