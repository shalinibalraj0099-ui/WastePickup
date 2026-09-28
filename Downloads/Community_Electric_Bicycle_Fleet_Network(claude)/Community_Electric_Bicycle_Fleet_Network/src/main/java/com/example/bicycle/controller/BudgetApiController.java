package com.example.bicycle.controller;

import com.example.bicycle.model.Budget;
import com.example.bicycle.repository.BudgetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Full CRUD REST API for Budget.
 * GET    /api/budgets       -> list all
 * GET    /api/budgets/{id} -> get one
 * POST   /api/budgets       -> create
 * PUT    /api/budgets/{id} -> update
 * DELETE /api/budgets/{id} -> delete
 */
@RestController
@RequestMapping("/api/budgets")
@CrossOrigin(origins = "*")
public class BudgetApiController {

    private final BudgetRepository repo;

    public BudgetApiController(BudgetRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Budget> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Budget> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Budget> create(@RequestBody Budget body) {
        body.setId(null);
        Budget saved = repo.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Budget> update(@PathVariable Long id, @RequestBody Budget body) {
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
