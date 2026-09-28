package com.example.bicycle.controller;

import com.example.bicycle.model.LedgerEntry;
import com.example.bicycle.repository.LedgerEntryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Full CRUD REST API for LedgerEntry.
 * GET    /api/ledger       -> list all
 * GET    /api/ledger/{id} -> get one
 * POST   /api/ledger       -> create
 * PUT    /api/ledger/{id} -> update
 * DELETE /api/ledger/{id} -> delete
 */
@RestController
@RequestMapping("/api/ledger")
@CrossOrigin(origins = "*")
public class LedgerApiController {

    private final LedgerEntryRepository repo;

    public LedgerApiController(LedgerEntryRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<LedgerEntry> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LedgerEntry> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LedgerEntry> create(@RequestBody LedgerEntry body) {
        body.setId(null);
        LedgerEntry saved = repo.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LedgerEntry> update(@PathVariable Long id, @RequestBody LedgerEntry body) {
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
