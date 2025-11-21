package com.servicio.reservas.agenda.infraestructure.persistence.reservations;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface SpringReservationRepository extends JpaRepository<ReservationModel, Long> {

    @NotNull Optional<ReservationModel> findById(@NotNull Long id);

    @Query("""

            SELECT r FROM ReservationModel r
    WHERE r.active = true
    AND r.status NOT IN ('CANCELED', 'COMPLETED')
    AND (
        r.date < :today
        OR (r.date = :today AND r.endTime < :now)
        ) """)
    List<ReservationModel> findAllActiveThatEnded(
            @Param("now") LocalTime now,
            @Param("today") LocalDate today);

//------------------------------------------------------------
    //usuario/cliente

    @Query("""
    
    SELECT r FROM ReservationModel r
    WHERE (:userId IS NULL OR r.userId = :userId)
    AND (:status IS NULL OR r.status = :status)
    AND (COALESCE(:startDate, r.date) <= r.date)
    AND (COALESCE(:endDate, r.date) >= r.date)
    ORDER BY r.date DESC
""")
    List<ReservationModel> searchByFilters(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") String status);

    //------------------------------------------------------------
            //administrador
    @Query("""
    SELECT r FROM ReservationModel  r
    WHERE (:userId IS NULL OR r.userId = :userId)
    AND (:serviceId IS NULL OR r.serviceId = :serviceId)
    AND (:status IS NULL OR r.status = :status)
    AND (COALESCE(:startDate, r.date) <= r.date)
    AND (COALESCE(:endDate, r.date) >= r.date)
    ORDER BY r.date DESC, r.startTime DESC
""")
    List<ReservationModel> searchAdminFilters(
            @Param("userId") Long userId,
            @Param("serviceId") Long serviceId,
            @Param("status") String status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
