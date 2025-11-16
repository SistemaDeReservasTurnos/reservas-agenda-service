package com.servicio.reservas.agenda.application.services;

import com.servicio.reservas.agenda.application.dto.RequestReservation;
import com.servicio.reservas.agenda.application.dto.ResponseReservation;
import com.servicio.reservas.agenda.domain.entities.Reservation;

public interface IReservationService {

    ResponseReservation createReservation(RequestReservation reservation);
    ResponseReservation editReservation(Long id, RequestReservation reservation);
    ResponseReservation findReservationById(Long id);

}
