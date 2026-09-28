package com.example.bicycle.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bicycle.dto.LoginRequest;
import com.example.bicycle.dto.RegisterRequest;
import com.example.bicycle.model.User;
import com.example.bicycle.repository.UserRepository;

/**
 * Serves the login/register pages AND exposes the REST auth API.
 *
 * REST API:
 *  POST /api/auth/register  {name, email, password}
 *  POST /api/auth/login     {email, password}
 *  GET  /api/users          -> list users (passwords masked)
 */
@Controller
public class AuthController {

    public AuthController() {
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @RestController
    @RequestMapping("/api/auth")
    @CrossOrigin(origins = "*")
    public static class AuthApi {

        private final UserRepository users;
        private final PasswordEncoder encoder;

        public AuthApi(UserRepository users, PasswordEncoder encoder) {
            this.users = users;
            this.encoder = encoder;
        }

        @PostMapping("/register")
        public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
            if (req.email() == null || req.email().isBlank() || req.password() == null || req.password().isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "name, email and password are required"));
            }
            if (users.existsByEmail(req.email())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Email already registered"));
            }
            User u = new User();
            u.setName(req.name());
            u.setEmail(req.email());
            u.setPassword(encoder.encode(req.password()));
            u.setRole("RIDER");
            User saved = users.save(u);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("id", saved.getId(), "name", saved.getName(), "email", saved.getEmail(), "role", saved.getRole()));
        }

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginRequest req) {
            return users.findByEmail(req.email())
                    .filter(u -> encoder.matches(req.password(), u.getPassword()))
                    .<ResponseEntity<?>>map(u -> ResponseEntity.ok(
                            Map.of("id", u.getId(), "name", u.getName(), "email", u.getEmail(), "role", u.getRole())))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid email or password")));
        }
    }

    @RestController
    @RequestMapping("/api/users")
    @CrossOrigin(origins = "*")
    public static class UserApi {
        private final UserRepository users;

        public UserApi(UserRepository users) {
            this.users = users;
        }

        @GetMapping
        public List<Map<String, Object>> all() {
            return users.findAll().stream()
                    .map(u -> Map.<String, Object>of("id", u.getId(), "name", u.getName(), "email", u.getEmail(), "role", u.getRole()))
                    .toList();
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Long id) {
            if (!users.existsById(id)) return ResponseEntity.notFound().build();
            users.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }
}
