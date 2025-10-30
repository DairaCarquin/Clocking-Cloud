package com.clockingcloud.domain.ports;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clockingcloud.domain.model.AttendanceEvent;

public interface AttendanceRepository extends JpaRepository<AttendanceEvent, Long> {
    Optional<AttendanceEvent> findTopByUserIdOrderByCheckInDesc(Long userId);

    Optional<AttendanceEvent> findByUserIdAndCheckInBetween(
            Long userId,
            LocalDateTime start,
            LocalDateTime end);

    List<AttendanceEvent> findAllByUserIdAndCheckInBetween(
            Long userId,
            LocalDateTime start,
            LocalDateTime end);
}