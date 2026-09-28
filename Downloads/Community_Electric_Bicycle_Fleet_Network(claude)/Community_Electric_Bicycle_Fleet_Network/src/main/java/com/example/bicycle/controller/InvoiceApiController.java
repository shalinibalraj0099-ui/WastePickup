package com.example.bicycle.controller;

import com.example.bicycle.model.CustomerInvoice;
import com.example.bicycle.repository.CustomerInvoiceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Full CRUD REST API for CustomerInvoice.
 * GET    /api/invoices       -> list all
 * GET    /api/invoices/{id} -> get one
 * POST   /api/invoices       -> create
 * PUT    /api/invoices/{id} -> update
 * DELETE /api/invoices/{id} -> delete
 */
@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "*")
public class InvoiceApiController {

    private final CustomerInvoiceRepository repo;

    public InvoiceApiController(CustomerInvoiceRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<CustomerInvoice> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerInvoice> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CustomerInvoice> create(@RequestBody CustomerInvoice body) {
        body.setId(null);
        CustomerInvoice saved = repo.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerInvoice> update(@PathVariable Long id, @RequestBody CustomerInvoice body) {
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
