package com.servicio.reservas.agenda.infraestructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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
    boolean existsOverlappingReservationCreate(
            @Param("barberId") Long barberId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query("""
    SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END
    FROM ShiftModel s
    WHERE s.barberId = :barberId
    AND s.date = :date
    AND (:startTime < s.timeEnd AND :endTime > s.timeStart)
    AND s.id <> :id
""")
    boolean existsOverlappingReservationUpdate(
            @Param("barberId") Long barberId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("id") Long id
    );

    List<ShiftModel> findByBarberIdAndDate(Long barberId, LocalDate date);

    @Transactional
    @Modifying
    @Query("""
    UPDATE ShiftModel s
    SET s.state = 'COMPLETED'
    WHERE s.reservationId = :reservationId
"""
    )
    void updateStateShiftByReservationId(@Param("reservationId") Long reservationId);

    @Modifying
    @Transactional
    @Query("""
    DELETE FROM ShiftModel s
    WHERE s.reservationId = :id
""")
    void deleteShiftFromReservation(@Param("id") Long id);
}
