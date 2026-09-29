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

import com.wastepickup.Services.ZoneService;
import com.wastepickup.entity.Zone;


@RestController
@RequestMapping("/api/zones")
public class ZoneController {


    private final ZoneService service;


    public ZoneController(ZoneService service) {
        this.service = service;
    }



    // CREATE ZONE
    @PostMapping
    public ResponseEntity<Zone> save(
            @RequestBody Zone zone) {

        Zone saved = service.createZone(zone);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }



    // GET ALL ZONES
    @GetMapping
    public ResponseEntity<List<Zone>> all() {

        return ResponseEntity
                .ok(service.getAllZones());
    }



    // GET ZONE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable Long id) {


        return ResponseEntity.ok(service.getZoneById(id));
    }




    // UPDATE ZONE
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody Zone zone) {


        return ResponseEntity.ok(service.updateZone(id, zone));
    }





    // DELETE ZONE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {


        service.deleteZone(id);
        return ResponseEntity
                .ok("Zone deleted successfully");
    }

}