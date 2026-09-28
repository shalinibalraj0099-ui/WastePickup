package com.example.bicycle.controller;

import com.example.bicycle.model.AnalyticAccount;
import com.example.bicycle.repository.AnalyticAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Full CRUD REST API for AnalyticAccount.
 * GET    /api/analytic-accounts       -> list all
 * GET    /api/analytic-accounts/{id} -> get one
 * POST   /api/analytic-accounts       -> create
 * PUT    /api/analytic-accounts/{id} -> update
 * DELETE /api/analytic-accounts/{id} -> delete
 */
@RestController
@RequestMapping("/api/analytic-accounts")
@CrossOrigin(origins = "*")
public class AnalyticAccountApiController {

    private final AnalyticAccountRepository repo;

    public AnalyticAccountApiController(AnalyticAccountRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<AnalyticAccount> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalyticAccount> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AnalyticAccount> create(@RequestBody AnalyticAccount body) {
        body.setId(null);
        AnalyticAccount saved = repo.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnalyticAccount> update(@PathVariable Long id, @RequestBody AnalyticAccount body) {
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
