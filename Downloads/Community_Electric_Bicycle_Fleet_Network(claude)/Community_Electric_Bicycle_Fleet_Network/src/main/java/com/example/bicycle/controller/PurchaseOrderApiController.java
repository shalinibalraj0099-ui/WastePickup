package com.example.bicycle.controller;

import com.example.bicycle.model.PurchaseOrder;
import com.example.bicycle.repository.PurchaseOrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Full CRUD REST API for PurchaseOrder.
 * GET    /api/purchase-orders       -> list all
 * GET    /api/purchase-orders/{id} -> get one
 * POST   /api/purchase-orders       -> create
 * PUT    /api/purchase-orders/{id} -> update
 * DELETE /api/purchase-orders/{id} -> delete
 */
@RestController
@RequestMapping("/api/purchase-orders")
@CrossOrigin(origins = "*")
public class PurchaseOrderApiController {

    private final PurchaseOrderRepository repo;

    public PurchaseOrderApiController(PurchaseOrderRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<PurchaseOrder> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrder> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PurchaseOrder> create(@RequestBody PurchaseOrder body) {
        body.setId(null);
        PurchaseOrder saved = repo.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrder> update(@PathVariable Long id, @RequestBody PurchaseOrder body) {
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
