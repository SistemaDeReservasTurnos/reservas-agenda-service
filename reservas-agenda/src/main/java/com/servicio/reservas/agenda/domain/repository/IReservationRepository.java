package com.servicio.reservas.agenda.domain.repository;

import com.servicio.reservas.agenda.application.dto.FilterReservationAdmin;
import com.servicio.reservas.agenda.domain.entities.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface IReservationRepository {

    Reservation save(Reservation reservation);
    Optional<Reservation> findByIdReservation(Long id); //buscar la reservacion a modificar
    List<Reservation> findAllActiveThatEnded(LocalTime now, LocalDate today);
    List<Reservation> adminSearchReservations(FilterReservationAdmin filters);

}
