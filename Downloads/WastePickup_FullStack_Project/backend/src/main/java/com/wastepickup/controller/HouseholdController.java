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
import org.springframework.web.server.ResponseStatusException;

import com.wastepickup.Services.HouseholdService;
import com.wastepickup.Services.SmsNotificationService;
import com.wastepickup.entity.Household;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/households")
public class HouseholdController {


    private final HouseholdService service;
        private final SmsNotificationService smsNotificationService;


        public HouseholdController(HouseholdService service, SmsNotificationService smsNotificationService) {
        this.service = service;
                this.smsNotificationService = smsNotificationService;
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

        @PostMapping("/{id}/notification/confirm")
        public ResponseEntity<String> confirmReminderSent(@PathVariable Long id) {
                try {
                        service.markReminderSent(id);
                        return ResponseEntity.ok("SMS marked as sent successfully");
                } catch (ResponseStatusException exception) {
                        return ResponseEntity.status(exception.getStatusCode()).body(exception.getReason());
                }
        }

        @PostMapping("/{id}/notification")
        public ResponseEntity<String> sendReminderNotification(@PathVariable Long id) {
                Household household = service.getHouseholdById(id);
                try {
                        smsNotificationService.sendPickupReminder(household);
                        return ResponseEntity.accepted().body("Pickup reminder SMS sent");
                } catch (ResponseStatusException exception) {
                        return ResponseEntity.status(exception.getStatusCode()).body(exception.getReason());
                }
        }


}