package com.clockingcloud.infrastructure.controller;

import com.clockingcloud.domain.model.WorkConfig;
import com.clockingcloud.infrastructure.persistence.WorkConfigJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config/work")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WorkConfigController {
    private final WorkConfigJpaRepository repo;

    @GetMapping
    public ResponseEntity<?> getConfig() {
        return ResponseEntity.ok(repo.findTopByOrderByIdDesc());
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody WorkConfig config) {
        return ResponseEntity.ok(repo.save(config));
    }
}