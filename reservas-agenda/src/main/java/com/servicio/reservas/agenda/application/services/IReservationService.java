package com.servicio.reservas.agenda.application.services;

import com.servicio.reservas.agenda.application.dto.RequestReservation;
import com.servicio.reservas.agenda.application.dto.ResponseReservation;

public interface IReservationService {
    ResponseReservation createReservation(RequestReservation reservation);

}
