package com.example.bicycle.controller;

import com.example.bicycle.model.Account;
import com.example.bicycle.repository.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Full CRUD REST API for Account.
 * GET    /api/accounts       -> list all
 * GET    /api/accounts/{id} -> get one
 * POST   /api/accounts       -> create
 * PUT    /api/accounts/{id} -> update
 * DELETE /api/accounts/{id} -> delete
 */
@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountApiController {

    private final AccountRepository repo;

    public AccountApiController(AccountRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Account> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Account> create(@RequestBody Account body) {
        body.setId(null);
        Account saved = repo.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> update(@PathVariable Long id, @RequestBody Account body) {
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
