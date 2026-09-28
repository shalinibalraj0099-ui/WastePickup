package com.example.bicycle.controller;

import com.example.bicycle.model.BikeLocation;
import com.example.bicycle.repository.BikeLocationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Full CRUD REST API for BikeLocation.
 * GET    /api/bike-locations       -> list all
 * GET    /api/bike-locations/{id} -> get one
 * POST   /api/bike-locations       -> create
 * PUT    /api/bike-locations/{id} -> update
 * DELETE /api/bike-locations/{id} -> delete
 */
@RestController
@RequestMapping("/api/bike-locations")
@CrossOrigin(origins = "*")
public class BikeLocationApiController {

    private final BikeLocationRepository repo;

    public BikeLocationApiController(BikeLocationRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<BikeLocation> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BikeLocation> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BikeLocation> create(@RequestBody BikeLocation body) {
        body.setId(null);
        BikeLocation saved = repo.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BikeLocation> update(@PathVariable Long id, @RequestBody BikeLocation body) {
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
