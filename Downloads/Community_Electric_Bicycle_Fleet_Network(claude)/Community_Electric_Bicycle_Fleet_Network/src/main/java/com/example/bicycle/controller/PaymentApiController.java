package com.example.bicycle.controller;

import com.example.bicycle.model.Payment;
import com.example.bicycle.repository.PaymentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Full CRUD REST API for Payment.
 * GET    /api/payments       -> list all
 * GET    /api/payments/{id} -> get one
 * POST   /api/payments       -> create
 * PUT    /api/payments/{id} -> update
 * DELETE /api/payments/{id} -> delete
 */
@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentApiController {

    private final PaymentRepository repo;

    public PaymentApiController(PaymentRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Payment> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Payment> create(@RequestBody Payment body) {
        body.setId(null);
        Payment saved = repo.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> update(@PathVariable Long id, @RequestBody Payment body) {
        return repo.findById(id).map(existing -> {
            body.setId(id);
            return ResponseEntity.ok(repo.save(body));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
