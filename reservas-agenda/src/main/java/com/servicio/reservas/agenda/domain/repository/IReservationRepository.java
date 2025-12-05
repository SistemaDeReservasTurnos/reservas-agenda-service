package com.servicio.reservas.agenda.domain.repository;

import com.servicio.reservas.agenda.application.dto.reservations.filters.FilterReservationAdmin;
import com.servicio.reservas.agenda.domain.entities.Reservation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface IReservationRepository {

    Reservation save(Reservation reservation);
    Optional<Reservation> findByIdReservation(Long id);
    List<Reservation> findAllActiveThatEnded(LocalTime now, LocalDate today);
    List<Reservation> userReservations(Long userId, LocalDate startDate, LocalDate endDate, String status); // Retrieve reservations for a user with optional date range and status filters
    List<Reservation> adminSearchReservations(FilterReservationAdmin filters);
    List<Reservation> findCompletedByDate(LocalDate startDate);

}

