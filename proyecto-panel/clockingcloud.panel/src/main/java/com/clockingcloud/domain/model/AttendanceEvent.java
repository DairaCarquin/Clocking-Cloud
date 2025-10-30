package com.clockingcloud.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "attendance_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Long workedSeconds;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;
}
