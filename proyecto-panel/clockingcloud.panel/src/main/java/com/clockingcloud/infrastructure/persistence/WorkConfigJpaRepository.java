package com.clockingcloud.infrastructure.persistence;

import com.clockingcloud.domain.model.WorkConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkConfigJpaRepository extends JpaRepository<WorkConfig, Long> {
    WorkConfig findTopByOrderByIdDesc();
}
