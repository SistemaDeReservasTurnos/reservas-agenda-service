package com.servicio.reservas.agenda.application.dto;

import com.servicio.reservas.agenda.domain.entities.Reservation;

public class ReservationMapper {

    public static ResponseReservation toResponse(Reservation reservation) {

        ResponseReservation responseReservation = new ResponseReservation();

        responseReservation.setId(reservation.getId());
        responseReservation.setServiceId(reservation.getServiceId());
        responseReservation.setUserId(reservation.getUserId());
        responseReservation.setBarberId(reservation.getBarberId());
        responseReservation.setDate(reservation.getDate());
        responseReservation.setTimeStart(reservation.getTimeStart());
        responseReservation.setTimeEnd(reservation.getTimeEnd());
        responseReservation.setStatus(reservation.getStatus());
        responseReservation.setActive(reservation.getActive());

        return responseReservation;
    }

}
