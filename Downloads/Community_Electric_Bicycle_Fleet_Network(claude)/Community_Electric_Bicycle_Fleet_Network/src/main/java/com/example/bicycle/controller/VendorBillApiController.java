package com.example.bicycle.controller;

import com.example.bicycle.model.VendorBill;
import com.example.bicycle.repository.VendorBillRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Full CRUD REST API for VendorBill.
 * GET    /api/vendor-bills       -> list all
 * GET    /api/vendor-bills/{id} -> get one
 * POST   /api/vendor-bills       -> create
 * PUT    /api/vendor-bills/{id} -> update
 * DELETE /api/vendor-bills/{id} -> delete
 */
@RestController
@RequestMapping("/api/vendor-bills")
@CrossOrigin(origins = "*")
public class VendorBillApiController {

    private final VendorBillRepository repo;

    public VendorBillApiController(VendorBillRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<VendorBill> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorBill> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<VendorBill> create(@RequestBody VendorBill body) {
        body.setId(null);
        VendorBill saved = repo.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendorBill> update(@PathVariable Long id, @RequestBody VendorBill body) {
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
