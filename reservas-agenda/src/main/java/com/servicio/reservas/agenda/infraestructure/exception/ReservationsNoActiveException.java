package com.servicio.reservas.agenda.infraestructure.exception;

public class ReservationsNoActiveException extends RuntimeException {
    public ReservationsNoActiveException(Long id) {

        super("The reservation ID " + id + " is deactivated.");
    }
}
