package com.servicio.reservas.agenda.domain.repository;

import com.servicio.reservas.agenda.domain.entities.Reservation;
import com.servicio.reservas.agenda.domain.entities.Shift;

import java.util.Optional;

public interface IReservationRepository {

    Reservation save(Reservation reservation);
    Optional<Reservation> findByIdReservation(Long id); //buscar la reservacion a modificar
}
