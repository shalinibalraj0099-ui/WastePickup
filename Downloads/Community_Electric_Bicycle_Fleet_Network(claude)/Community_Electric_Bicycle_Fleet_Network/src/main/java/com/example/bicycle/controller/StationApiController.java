package com.example.bicycle.controller;

import com.example.bicycle.model.DockStation;
import com.example.bicycle.repository.DockStationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Full CRUD REST API for DockStation.
 * GET    /api/stations       -> list all
 * GET    /api/stations/{id} -> get one
 * POST   /api/stations       -> create
 * PUT    /api/stations/{id} -> update
 * DELETE /api/stations/{id} -> delete
 */
@RestController
@RequestMapping("/api/stations")
@CrossOrigin(origins = "*")
public class StationApiController {

    private final DockStationRepository repo;

    public StationApiController(DockStationRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<DockStation> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DockStation> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DockStation> create(@RequestBody DockStation body) {
        body.setId(null);
        DockStation saved = repo.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DockStation> update(@PathVariable Long id, @RequestBody DockStation body) {
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
