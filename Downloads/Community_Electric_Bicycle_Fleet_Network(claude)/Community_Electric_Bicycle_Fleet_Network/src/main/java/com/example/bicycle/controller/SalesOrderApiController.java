package com.example.bicycle.controller;

import com.example.bicycle.model.SalesOrder;
import com.example.bicycle.repository.SalesOrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Full CRUD REST API for SalesOrder.
 * GET    /api/sales-orders       -> list all
 * GET    /api/sales-orders/{id} -> get one
 * POST   /api/sales-orders       -> create
 * PUT    /api/sales-orders/{id} -> update
 * DELETE /api/sales-orders/{id} -> delete
 */
@RestController
@RequestMapping("/api/sales-orders")
@CrossOrigin(origins = "*")
public class SalesOrderApiController {

    private final SalesOrderRepository repo;

    public SalesOrderApiController(SalesOrderRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<SalesOrder> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesOrder> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SalesOrder> create(@RequestBody SalesOrder body) {
        body.setId(null);
        SalesOrder saved = repo.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesOrder> update(@PathVariable Long id, @RequestBody SalesOrder body) {
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
