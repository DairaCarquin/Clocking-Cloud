package com.clockingcloud.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.clockingcloud.application.service.UserService;
import com.clockingcloud.domain.model.User;
import com.clockingcloud.infrastructure.security.JwtUtil;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        return userService.login(email, password)
                .<ResponseEntity<?>>map(user -> {
                    String accessToken = jwtUtil.generateToken(user.getEmail(), 15);
                    String refreshToken = jwtUtil.generateToken(user.getEmail(), 10080);
                    return ResponseEntity.ok(Map.of(
                            "accessToken", accessToken,
                            "refreshToken", refreshToken,
                            "user", user));
                })
                .orElseGet(() -> ResponseEntity.status(401).body("Credenciales inválidas"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        if (!jwtUtil.isTokenValid(refreshToken)) {
            return ResponseEntity.status(401).body("Token inválido o expirado");
        }
        String email = jwtUtil.extractEmail(refreshToken);
        String newAccess = jwtUtil.generateToken(email, 15);
        return ResponseEntity.ok(Map.of("accessToken", newAccess));
    }

}
