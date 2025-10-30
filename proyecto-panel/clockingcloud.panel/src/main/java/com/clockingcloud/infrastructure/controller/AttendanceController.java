package com.clockingcloud.infrastructure.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.clockingcloud.application.service.AttendanceService;
import com.clockingcloud.domain.model.AttendanceEvent;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService service;

    @PostMapping("/checkin")
    public ResponseEntity<?> checkIn(@RequestParam Long userId) {
        if (userId == null || userId <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "El parámetro userId es obligatorio y debe ser mayor a 0"));
        }

        try {
            AttendanceEvent event = service.registerCheckIn(userId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "Entrada registrada correctamente",
                            "evento", event));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Error interno al registrar la entrada"));
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkOut(@RequestParam Long userId) {
        if (userId == null || userId <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "El parámetro userId es obligatorio y debe ser mayor a 0"));
        }

        try {
            AttendanceEvent event = service.registerCheckOut(userId);
            return ResponseEntity.ok(Map.of(
                    "message", "Salida registrada correctamente",
                    "evento", event));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Error interno al registrar la salida"));
        }
    }

    @GetMapping("/last")
    public ResponseEntity<?> lastEvent(@RequestParam Long userId) {
        if (userId == null || userId <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "El parámetro userId es obligatorio y debe ser mayor a 0"));
        }

        return service.getLastEvent(userId)
                .map(event -> ResponseEntity.ok(Map.of(
                        "entrada", service.formatTime(event.getCheckIn()),
                        "salida", service.formatTime(event.getCheckOut()),
                        "horasTrabajadas", service.formatDuration(event.getWorkedSeconds()))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "No existen eventos registrados para este usuario")));
    }

    @GetMapping("/monthly")
    public ResponseEntity<?> getMonthlyAttendance(
            @RequestParam Long userId,
            @RequestParam int year,
            @RequestParam int month) {
        if (userId == null || userId <= 0) {
            return ResponseEntity.badRequest().body(Map.of("message", "El parámetro userId es obligatorio"));
        }

        try {
            return ResponseEntity.ok(service.getMonthlyAttendance(userId, year, month));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

}
