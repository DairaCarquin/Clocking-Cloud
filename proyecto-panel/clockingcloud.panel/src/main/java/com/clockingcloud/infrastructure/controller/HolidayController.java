package com.clockingcloud.infrastructure.controller;

import com.clockingcloud.domain.model.Holiday;
import com.clockingcloud.infrastructure.persistence.HolidayJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/holidays")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HolidayController {
    private final HolidayJpaRepository repo;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(repo.findAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Holiday holiday) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(holiday));
    }
}
