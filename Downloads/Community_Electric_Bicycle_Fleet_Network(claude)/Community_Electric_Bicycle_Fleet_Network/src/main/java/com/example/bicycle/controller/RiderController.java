package com.example.bicycle.controller;

import com.example.bicycle.model.Rider;
import com.example.bicycle.repository.RiderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Full CRUD REST API for Riders (Users / Customers module).
 * GET    /api/riders       -> list all
 * GET    /api/riders/{id}  -> get one
 * POST   /api/riders       -> create
 * PUT    /api/riders/{id}  -> update
 * DELETE /api/riders/{id}  -> delete
 */
@RestController
@RequestMapping("/api/riders")
@CrossOrigin(origins = "*")
public class RiderController {

    private final RiderRepository repo;

    public RiderController(RiderRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Rider> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rider> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Rider> create(@RequestBody Rider body) {
        body.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(body));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rider> update(@PathVariable Long id, @RequestBody Rider body) {
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
