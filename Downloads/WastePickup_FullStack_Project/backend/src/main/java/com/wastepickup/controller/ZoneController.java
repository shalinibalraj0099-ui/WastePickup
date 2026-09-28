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

import com.wastepickup.entity.Zone;
import com.wastepickup.repository.ZoneRepository;


@RestController
@RequestMapping("/api/zones")
public class ZoneController {


    private final ZoneRepository repo;


    public ZoneController(ZoneRepository repo) {
        this.repo = repo;
    }



    // CREATE ZONE
    @PostMapping
    public ResponseEntity<Zone> save(
            @RequestBody Zone zone) {

        Zone saved = repo.save(zone);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }



    // GET ALL ZONES
    @GetMapping
    public ResponseEntity<List<Zone>> all() {

        return ResponseEntity
                .ok(repo.findAll());
    }



    // GET ZONE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable Long id) {


        return repo.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Zone not found"));
    }




    // UPDATE ZONE
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody Zone zone) {


        return repo.findById(id)
                .<ResponseEntity<?>>map(existing -> {
                    existing.setName(zone.getName());
                    return ResponseEntity.ok(repo.save(existing));
                })
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Zone not found"));
    }





    // DELETE ZONE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {


        if(!repo.existsById(id)) {

            return ResponseEntity
                    .status(404)
                    .body("Zone not found");
        }


        repo.deleteById(id);


        return ResponseEntity
                .ok("Zone deleted successfully");
    }

}