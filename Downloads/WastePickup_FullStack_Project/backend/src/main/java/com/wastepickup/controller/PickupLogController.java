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

import com.wastepickup.Services.PickupLogService;
import com.wastepickup.entity.PickupLog;

@RestController
@RequestMapping("/api/pickups")
public class PickupLogController {

    private final PickupLogService service;


    public PickupLogController(PickupLogService service) {
        this.service = service;
    }


    // CREATE PICKUP LOG
    @PostMapping
    public ResponseEntity<?> save(@RequestBody PickupLog p) {

        PickupLog saved = service.createPickupLog(p);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }



    // GET ALL PICKUP LOGS
    @GetMapping
    public ResponseEntity<List<PickupLog>> all() {

        return ResponseEntity.ok(service.getAllPickupLogs());
    }



    // GET PICKUP LOG BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getPickupLogById(id));
    }




    // UPDATE PICKUP LOG
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody PickupLog p) {


        return ResponseEntity.ok(service.updatePickupLog(id, p));
    }




    // DELETE PICKUP LOG
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id) {


        service.deletePickupLog(id);
        return ResponseEntity
                .ok("Pickup log deleted successfully");
    }

}