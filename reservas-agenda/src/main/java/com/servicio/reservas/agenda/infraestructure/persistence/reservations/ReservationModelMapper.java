package com.servicio.reservas.agenda.infraestructure.persistence.reservations;

import com.servicio.reservas.agenda.domain.entities.Reservation;

public class ReservationModelMapper {

    public static ReservationModel toModel(Reservation reservation) {

        ReservationModel reservationModel = new ReservationModel();
        reservationModel.setId(reservation.getId());
        reservationModel.setServiceId(reservation.getServiceId());
        reservationModel.setUserId(reservation.getUserId());
        reservationModel.setBarberId(reservation.getBarberId());
        reservationModel.setDate(reservation.getDate());
        reservationModel.setStartTime(reservation.getTimeStart());
        reservationModel.setEndTime(reservation.getTimeEnd());
        reservationModel.setStatus("Reserved");
        reservationModel.setActive(true);

        return reservationModel;
    }

    public static Reservation toDomain(ReservationModel reservationModel) {

        Reservation reservation = new Reservation();

        reservation.setId(reservationModel.getId());
        reservation.setServiceId(reservationModel.getServiceId());
        reservation.setUserId(reservationModel.getUserId());
        reservation.setBarberId(reservationModel.getBarberId());
        reservation.setDate(reservationModel.getDate());
        reservation.setTimeStart(reservationModel.getStartTime());
        reservation.setTimeEnd(reservationModel.getEndTime());
        reservation.setStatus(reservationModel.getStatus());
        reservation.setActive(reservationModel.getActive());

        return reservation;
    }
}
