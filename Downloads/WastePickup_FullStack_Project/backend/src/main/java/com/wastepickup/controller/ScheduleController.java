package com.wastepickup.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wastepickup.Services.ScheduleService;
import com.wastepickup.entity.Schedule;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {


    private final ScheduleService service;


    public ScheduleController(ScheduleService service) {
        this.service = service;
    }



    // Create new schedule
    @PostMapping
    public ResponseEntity<Schedule> createSchedule(
            @Valid @RequestBody Schedule schedule) {

        Schedule saved =
                service.createSchedule(schedule);

        return new ResponseEntity<>(
                saved,
                HttpStatus.CREATED
        );
    }




    // Get all schedules
    @GetMapping
    public ResponseEntity<List<Schedule>> getAllSchedules(){

        return ResponseEntity.ok(
                service.getAllSchedules()
        );
    }




    // Get schedule by id
    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getScheduleById(
            @PathVariable Long id){

        return ResponseEntity.ok(
                service.getScheduleById(id)
        );
    }





    // Update schedule
    @PutMapping("/{id}")
    public ResponseEntity<Schedule> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody Schedule schedule){


        Schedule updated =
                service.updateSchedule(id, schedule);


        return ResponseEntity.ok(updated);
    }





    // Delete schedule
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSchedule(
            @PathVariable Long id){


        service.deleteSchedule(id);


        return ResponseEntity.ok(
                "Schedule deleted successfully"
        );
    }


}
