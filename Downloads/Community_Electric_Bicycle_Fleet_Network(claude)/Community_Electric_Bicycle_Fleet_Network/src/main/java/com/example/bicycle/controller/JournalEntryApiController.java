package com.example.bicycle.controller;

import com.example.bicycle.model.JournalEntry;
import com.example.bicycle.repository.JournalEntryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Full CRUD REST API for JournalEntry.
 * GET    /api/journal-entries       -> list all
 * GET    /api/journal-entries/{id} -> get one
 * POST   /api/journal-entries       -> create
 * PUT    /api/journal-entries/{id} -> update
 * DELETE /api/journal-entries/{id} -> delete
 */
@RestController
@RequestMapping("/api/journal-entries")
@CrossOrigin(origins = "*")
public class JournalEntryApiController {

    private final JournalEntryRepository repo;

    public JournalEntryApiController(JournalEntryRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<JournalEntry> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalEntry> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<JournalEntry> create(@RequestBody JournalEntry body) {
        body.setId(null);
        JournalEntry saved = repo.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JournalEntry> update(@PathVariable Long id, @RequestBody JournalEntry body) {
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
