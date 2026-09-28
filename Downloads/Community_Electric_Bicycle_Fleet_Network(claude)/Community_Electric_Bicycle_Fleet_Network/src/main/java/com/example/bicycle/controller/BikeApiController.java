package com.example.bicycle.controller;

import com.example.bicycle.model.EBike;
import com.example.bicycle.repository.EBikeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Full CRUD REST API for EBike.
 * GET    /api/bikes       -> list all
 * GET    /api/bikes/{id} -> get one
 * POST   /api/bikes       -> create
 * PUT    /api/bikes/{id} -> update
 * DELETE /api/bikes/{id} -> delete
 */
@RestController
@RequestMapping("/api/bikes")
@CrossOrigin(origins = "*")
public class BikeApiController {

    private final EBikeRepository repo;

    public BikeApiController(EBikeRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<EBike> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EBike> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EBike> create(@RequestBody EBike body) {
        body.setId(null);
        EBike saved = repo.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EBike> update(@PathVariable Long id, @RequestBody EBike body) {
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
