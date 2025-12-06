package com.servicio.reservas.agenda.infraestructure.exception;

public class TechnicalException extends RuntimeException {
    public TechnicalException(String message, Throwable cause) {
        super(message,  cause);
    }
}
