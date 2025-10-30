package com.clockingcloud.domain.ports;

import com.clockingcloud.domain.model.Holiday;

public interface HolidayRepository {
    void save(Holiday holiday);
    Iterable<Holiday> findAll();
}
