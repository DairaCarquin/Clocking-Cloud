package com.clockingcloud.domain.ports;

import com.clockingcloud.domain.model.WorkConfig;

public interface WorkConfigRepository {
    WorkConfig save(WorkConfig config);
    WorkConfig findTopByOrderByIdDesc();
}
