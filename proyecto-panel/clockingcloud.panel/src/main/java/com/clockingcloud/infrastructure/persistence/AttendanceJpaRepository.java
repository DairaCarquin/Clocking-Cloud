package com.clockingcloud.infrastructure.persistence;

import com.clockingcloud.domain.model.AttendanceEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.*;

public interface AttendanceJpaRepository extends JpaRepository<AttendanceEvent, Long> {
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