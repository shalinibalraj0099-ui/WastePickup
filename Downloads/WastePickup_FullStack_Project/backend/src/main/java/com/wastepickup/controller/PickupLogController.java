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

import com.wastepickup.entity.PickupLog;
import com.wastepickup.repository.PickupLogRepository;

@RestController
@RequestMapping("/api/pickups")
public class PickupLogController {

    private final PickupLogRepository repo;


    public PickupLogController(PickupLogRepository repo) {
        this.repo = repo;
    }


    // CREATE PICKUP LOG
    @PostMapping
    public ResponseEntity<?> save(@RequestBody PickupLog p) {

        if (p.getScore() == null ||
            p.getScore() < 0 ||
            p.getScore() > 100) {

            return ResponseEntity
                    .badRequest()
                    .body("Segregation score must be between 0 and 100");
        }

        PickupLog saved = repo.save(p);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }



    // GET ALL PICKUP LOGS
    @GetMapping
    public ResponseEntity<List<PickupLog>> all() {

        return ResponseEntity
                .ok(repo.findAll());
    }



    // GET PICKUP LOG BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable Long id) {

        return repo.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Pickup log not found with id : " + id));
    }




    // UPDATE PICKUP LOG
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody PickupLog p) {


        if(p.getScore() == null ||
           p.getScore() < 0 ||
           p.getScore() > 100) {


            return ResponseEntity
                    .badRequest()
                    .body("Segregation score must be between 0 and 100");
        }


        return repo.findById(id)
                .<ResponseEntity<?>>map(existing -> {
                    existing.setScore(p.getScore());
                    existing.setPickupTime(p.getPickupTime());
                    existing.setHousehold(p.getHousehold());
                    return ResponseEntity.ok(repo.save(existing));
                })
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Pickup log not found with id : " + id));
    }




    // DELETE PICKUP LOG
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id) {


        if(!repo.existsById(id)) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Pickup log not found with id : " + id);
        }


        repo.deleteById(id);


        return ResponseEntity
                .ok("Pickup log deleted successfully");
    }

}