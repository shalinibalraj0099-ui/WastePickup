package com.example.bicycle.controller;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bicycle.dto.EndRideRequest;
import com.example.bicycle.dto.StartRideRequest;
import com.example.bicycle.model.Payment;
import com.example.bicycle.model.Ride;
import com.example.bicycle.repository.PaymentRepository;
import com.example.bicycle.repository.RideRepository;
import com.example.bicycle.service.RideService;

/**
 * REST API for Rides.
 * GET    /api/rides         -> list all
 * GET    /api/rides/{id}    -> get one
 * POST   /api/rides         -> create a ride record directly
 * POST   /api/rides/start   -> start a ride (unlock bike)  {riderId, bikeId}
 * POST   /api/rides/end     -> end a ride (auto fare calc) {rideId}
 * PUT    /api/rides/{id}    -> update
 * DELETE /api/rides/{id}    -> delete
 */
@RestController
@RequestMapping("/api/rides")
@CrossOrigin(origins = "*")
public class RideController {

    private final RideService service;
    private final RideRepository repo;
    private final PaymentRepository payments;

    public RideController(RideService service, RideRepository repo, PaymentRepository payments) {
        this.service = service;
        this.repo = repo;
        this.payments = payments;
    }

    @GetMapping
    public List<Ride> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ride> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ride> create(@RequestBody Ride body) {
        body.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(body));
    }

    @PostMapping("/start")
    public ResponseEntity<?> start(@RequestBody StartRideRequest x) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.start(x.riderId(), x.bikeId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/end")
    public ResponseEntity<?> end(@RequestBody EndRideRequest x) {
        try {
            Ride ride = service.end(x.rideId());
            Payment payment = payments.findAll().stream()
                    .filter(p -> "RIDE".equalsIgnoreCase(p.getReferenceType()) && x.rideId().equals(p.getReferenceId()))
                    .findFirst()
                    .orElseGet(() -> {
                        Payment created = new Payment();
                        created.setPaymentType("INCOME");
                        created.setReferenceType("RIDE");
                        created.setReferenceId(ride.getId());
                        created.setAmount(ride.getFare());
                        created.setMethod(x.method() == null || x.method().isBlank() ? "CARD" : x.method());
                        created.setPaymentDate(LocalDate.now());
                        return payments.save(created);
                    });
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("ride", ride);
            result.put("fare", ride.getFare());
            result.put("payment", payment);
            result.put("paymentStatus", "SUCCESS");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ride> update(@PathVariable Long id, @RequestBody Ride body) {
        return repo.findById(id).map(existing -> {
            body.setId(id);
            return ResponseEntity.ok(repo.save(body));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
