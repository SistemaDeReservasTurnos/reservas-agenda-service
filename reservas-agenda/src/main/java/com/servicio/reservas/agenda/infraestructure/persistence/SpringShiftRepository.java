package com.servicio.reservas.agenda.infraestructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface SpringShiftRepository extends JpaRepository<ShiftModel, Long> {

    @Query("""
    SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END
    FROM ShiftModel s
    WHERE s.barberId = :barberId
    AND s.date = :date
    AND (:startTime < s.timeEnd AND :endTime > s.timeStart)
""")
    boolean existsOverlappingReservation(
            @Param("barberId") Long barberId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    List<ShiftModel> findByBarberIdAndDate(Long barberId, LocalDate date);
}
