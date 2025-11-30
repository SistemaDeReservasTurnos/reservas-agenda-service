package com.servicio.reservas.agenda.application.dto.reservations;

import com.servicio.reservas.agenda.domain.entities.Reservation;
import java.time.LocalTime;

public class ReservationMapper {

    public static ResponseReservation toResponse(
            Reservation reservation,
            String userName,
            String barberName) {

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
        responseReservation.setAmount(reservation.getAmount());

        responseReservation.setNameBarber(barberName);
        responseReservation.setNameUser(userName);

        return responseReservation;
    }

    public static Reservation toDomain(RequestReservation request, LocalTime endTime, Double amount) {

        Reservation reservation = new Reservation();

        reservation.setServiceId(request.getServiceId());
        reservation.setUserId(request.getUserId());
        reservation.setBarberId(request.getBarberId());
        reservation.setDate(request.getDate());
        reservation.setTimeStart(request.getTimeStart());
        reservation.setTimeEnd(endTime);

        reservation.setStatus("RESERVED");
        reservation.setActive(true);
        reservation.setAmount(amount);
        return reservation;
    }
}
