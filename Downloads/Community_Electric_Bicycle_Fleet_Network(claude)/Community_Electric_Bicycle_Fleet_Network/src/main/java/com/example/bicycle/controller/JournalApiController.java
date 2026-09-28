package com.example.bicycle.controller;

import com.example.bicycle.model.Journal;
import com.example.bicycle.repository.JournalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Full CRUD REST API for Journal.
 * GET    /api/journals       -> list all
 * GET    /api/journals/{id} -> get one
 * POST   /api/journals       -> create
 * PUT    /api/journals/{id} -> update
 * DELETE /api/journals/{id} -> delete
 */
@RestController
@RequestMapping("/api/journals")
@CrossOrigin(origins = "*")
public class JournalApiController {

    private final JournalRepository repo;

    public JournalApiController(JournalRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Journal> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Journal> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Journal> create(@RequestBody Journal body) {
        body.setId(null);
        Journal saved = repo.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Journal> update(@PathVariable Long id, @RequestBody Journal body) {
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
