package com.clockingcloud.application.service;

import com.clockingcloud.domain.model.*;
import com.clockingcloud.infrastructure.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceJpaRepository attendanceRepo;
    private final UserJpaRepository userRepo;
    private final HolidayJpaRepository holidayRepo;
    private final WorkConfigJpaRepository configRepo;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");

    public AttendanceEvent registerCheckIn(Long userId) {
        LocalDate today = LocalDate.now();

        Optional<AttendanceEvent> todayEvent = attendanceRepo.findTopByUserIdOrderByCheckInDesc(userId)
                .filter(e -> e.getCheckIn().toLocalDate().equals(today));

        if (todayEvent.isPresent()) {
            throw new IllegalStateException("Ya registraste tu entrada hoy.");
        }

        AttendanceEvent event = new AttendanceEvent();
        event.setUserId(userId);
        event.setCheckIn(LocalDateTime.now());

        return attendanceRepo.save(event);
    }

    public AttendanceEvent registerCheckOut(Long userId) {
        LocalDate today = LocalDate.now();
        Optional<AttendanceEvent> opt = attendanceRepo.findTopByUserIdOrderByCheckInDesc(userId);

        if (opt.isEmpty()) throw new IllegalStateException("Primero debes registrar tu entrada.");

        AttendanceEvent event = opt.get();
        if (!event.getCheckIn().toLocalDate().equals(today))
            throw new IllegalStateException("No hay asistencia activa hoy.");

        if (event.getCheckOut() != null)
            throw new IllegalStateException("Ya registraste tu salida hoy.");

        event.setCheckOut(LocalDateTime.now());
        event.setWorkedSeconds(Duration.between(event.getCheckIn(), event.getCheckOut()).getSeconds());

        return attendanceRepo.save(event);
    }

    public Optional<AttendanceEvent> getLastEvent(Long userId) {
        return attendanceRepo.findTopByUserIdOrderByCheckInDesc(userId);
    }

    public String formatTime(LocalDateTime time) {
        return (time == null) ? "--:--:--" : time.format(timeFormatter);
    }

    public String formatDuration(Long seconds) {
        if (seconds == null) return "--:--:--";
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    // 🔹 Lógica dinámica mensual
    public List<Map<String, Object>> getMonthlyAttendance(Long userId, int year, int month) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));

        WorkConfig config = Optional.ofNullable(configRepo.findTopByOrderByIdDesc())
                .orElse(new WorkConfig(1L, LocalTime.of(8, 0), LocalTime.of(18, 30), 30));

        LocalDate firstDay = LocalDate.of(year, month, 1);
        int totalDays = firstDay.lengthOfMonth();
        List<Map<String, Object>> result = new ArrayList<>();

        for (int d = 1; d <= totalDays; d++) {
            LocalDate date = LocalDate.of(year, month, d);
            Map<String, Object> dayInfo = new HashMap<>();
            dayInfo.put("date", date.toString());
            dayInfo.put("dayOfWeek", date.getDayOfWeek().toString());

            // Si la fecha es antes de la entrada del usuario → no mostrar como ausente
            if (user.getEntryDate() != null && date.isBefore(user.getEntryDate())) {
                dayInfo.put("status", "NONE");
                result.add(dayInfo);
                continue;
            }

            // Día festivo
            if (holidayRepo.findByDate(date).isPresent()) {
                dayInfo.put("status", AttendanceStatus.HOLIDAY.name());
                result.add(dayInfo);
                continue;
            }

            Optional<AttendanceEvent> optEvent = attendanceRepo.findByUserIdAndCheckInBetween(
                    userId, date.atStartOfDay(), date.atTime(23, 59, 59));

            AttendanceStatus status;
            if (optEvent.isEmpty()) {
                status = AttendanceStatus.ABSENT;
            } else {
                AttendanceEvent event = optEvent.get();
                LocalTime checkIn = event.getCheckIn().toLocalTime();
                LocalTime checkOut = event.getCheckOut() != null
                        ? event.getCheckOut().toLocalTime() : null;
                long worked = event.getWorkedSeconds() != null ? event.getWorkedSeconds() : 0;
                long hours = worked / 3600;

                if (checkIn.isAfter(config.getTimeEntry().plusMinutes(config.getToleranceMinutes()))) {
                    status = AttendanceStatus.LATE;
                } else if (checkOut != null && checkOut.isBefore(config.getTimeDeparture().minusHours(1))) {
                    status = AttendanceStatus.EARLY_LEAVE;
                } else if (hours >= 8) {
                    status = AttendanceStatus.ON_TIME;
                } else if (hours >= 4) {
                    status = AttendanceStatus.HALF_DAY;
                } else {
                    status = AttendanceStatus.ABSENT;
                }
            }
            dayInfo.put("status", status.name());
            result.add(dayInfo);
        }
        return result;
    }
}
