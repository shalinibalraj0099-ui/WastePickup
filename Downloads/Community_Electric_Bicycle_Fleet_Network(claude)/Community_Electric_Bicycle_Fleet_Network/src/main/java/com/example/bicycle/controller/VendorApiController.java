package com.example.bicycle.controller;

import com.example.bicycle.model.Vendor;
import com.example.bicycle.repository.VendorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Full CRUD REST API for Vendor.
 * GET    /api/vendors       -> list all
 * GET    /api/vendors/{id} -> get one
 * POST   /api/vendors       -> create
 * PUT    /api/vendors/{id} -> update
 * DELETE /api/vendors/{id} -> delete
 */
@RestController
@RequestMapping("/api/vendors")
@CrossOrigin(origins = "*")
public class VendorApiController {

    private final VendorRepository repo;

    public VendorApiController(VendorRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Vendor> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vendor> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Vendor> create(@RequestBody Vendor body) {
        body.setId(null);
        Vendor saved = repo.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vendor> update(@PathVariable Long id, @RequestBody Vendor body) {
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
