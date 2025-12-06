package com.servicio.reservas.agenda.application.dto.reservations;

import lombok.Data;

@Data
public class CancelReservationEvent {
    private Long reservationId;
}
