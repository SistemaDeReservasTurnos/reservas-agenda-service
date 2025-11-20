package com.servicio.reservas.agenda.application.services;

import com.servicio.reservas.agenda.application.dto.FilterReservationUser;
import com.servicio.reservas.agenda.application.dto.RequestReservation;
import com.servicio.reservas.agenda.application.dto.ResponseReservation;
import com.servicio.reservas.agenda.domain.entities.Reservation;
import com.servicio.reservas.agenda.application.dto.FilterReservationAdmin;

import java.util.List;

public interface IReservationService {
    ResponseReservation createReservation(RequestReservation reservation);
    ResponseReservation editReservation(Long id, RequestReservation reservation);
    ResponseReservation findReservationById(Long id);
    void deleteReservation(Long id);
    void cancelReservation(Long id);
    void deactivateReservation(Long id);


    //Consulta de todas las reservas (solo administradores)
    List<ResponseReservation> searchAllReservations(FilterReservationAdmin filters);

}
