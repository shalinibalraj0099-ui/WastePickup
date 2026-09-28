package com.wastepickup.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wastepickup.entity.Household;
import com.wastepickup.Services.HouseholdService;

import jakarta.validation.Valid;

import java.util.List;


@RestController
@RequestMapping("/api/households")
public class HouseholdController {


    private final HouseholdService service;


    public HouseholdController(HouseholdService service) {
        this.service = service;
    }



    // Create Household
    @PostMapping
    public ResponseEntity<Household> createHousehold(
            @Valid @RequestBody Household household) {


        Household saved =
                service.createHousehold(household);


        return new ResponseEntity<>(
                saved,
                HttpStatus.CREATED
        );
    }





    // Get all households
    @GetMapping
    public ResponseEntity<List<Household>> getAllHouseholds() {


        return ResponseEntity.ok(
                service.getAllHouseholds()
        );
    }





    // Get household by ID
    @GetMapping("/{id}")
    public ResponseEntity<Household> getHouseholdById(
            @PathVariable Long id) {


        return ResponseEntity.ok(
                service.getHouseholdById(id)
        );
    }





    // Get households by zone
    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<Household>> getHouseholdsByZone(
            @PathVariable Long zoneId) {


        return ResponseEntity.ok(
                service.getHouseholdsByZone(zoneId)
        );
    }





    // Update household
    @PutMapping("/{id}")
    public ResponseEntity<Household> updateHousehold(
            @PathVariable Long id,
            @Valid @RequestBody Household household) {


        Household updated =
                service.updateHousehold(id, household);


        return ResponseEntity.ok(updated);
    }





    // Delete household
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHousehold(
            @PathVariable Long id) {


        service.deleteHousehold(id);


        return ResponseEntity.ok(
                "Household deleted successfully"
        );
    }





    // Get household segregation average score
    @GetMapping("/{id}/average-score")
    public ResponseEntity<Double> getAverageScore(
            @PathVariable Long id) {


        return ResponseEntity.ok(
                service.calculateAverageScore(id)
        );
    }





    // Get flagged households
    @GetMapping("/flagged")
    public ResponseEntity<List<Household>> getFlaggedHouseholds() {


        return ResponseEntity.ok(
                service.getFlaggedHouseholds()
        );
    }


}