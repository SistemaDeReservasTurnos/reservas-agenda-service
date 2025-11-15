package com.servicio.reservas.agenda.domain.repository;

import com.servicio.reservas.agenda.domain.entities.Reservation;

public interface IReservationRepository {

    Reservation save(Reservation reservation);
}
