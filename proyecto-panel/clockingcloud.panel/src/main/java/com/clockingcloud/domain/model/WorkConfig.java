package com.clockingcloud.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "work_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime timeEntry;
    private LocalTime timeDeparture;
    private int toleranceMinutes;
}
