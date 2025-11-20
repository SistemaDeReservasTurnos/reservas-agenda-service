package com.servicio.reservas.agenda.infraestructure.persistence.reservations;

import com.servicio.reservas.agenda.domain.entities.Reservation;
import com.servicio.reservas.agenda.infraestructure.persistence.reservations.Specifications.SearchReservationUserSpecification;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface SpringReservationRepository extends JpaRepository<ReservationModel, Long>, JpaSpecificationExecutor<ReservationModel> {

    @NotNull Optional<ReservationModel> findById(@NotNull Long id);

    @Query("""

            SELECT r FROM reservation r
    WHERE r.active = true
    AND r.status NOT IN ('CANCELED', 'COMPLETED')
    AND (
        r.date < :today
        OR (r.date = :today AND r.endTime < :now)
        ) """)
    List<ReservationModel> findAllActiveThatEnded(
            @Param("now") LocalTime now,
            @Param("today") LocalDate today);

}
