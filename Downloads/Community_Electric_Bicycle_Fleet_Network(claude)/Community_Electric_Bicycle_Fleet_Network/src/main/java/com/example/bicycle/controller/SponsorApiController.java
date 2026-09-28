package com.example.bicycle.controller;

import com.example.bicycle.model.CorporateSponsor;
import com.example.bicycle.repository.CorporateSponsorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Full CRUD REST API for CorporateSponsor.
 * GET    /api/sponsors       -> list all
 * GET    /api/sponsors/{id} -> get one
 * POST   /api/sponsors       -> create
 * PUT    /api/sponsors/{id} -> update
 * DELETE /api/sponsors/{id} -> delete
 */
@RestController
@RequestMapping("/api/sponsors")
@CrossOrigin(origins = "*")
public class SponsorApiController {

    private final CorporateSponsorRepository repo;

    public SponsorApiController(CorporateSponsorRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<CorporateSponsor> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CorporateSponsor> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CorporateSponsor> create(@RequestBody CorporateSponsor body) {
        body.setId(null);
        CorporateSponsor saved = repo.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CorporateSponsor> update(@PathVariable Long id, @RequestBody CorporateSponsor body) {
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
