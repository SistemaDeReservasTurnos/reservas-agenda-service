package com.servicio.reservas.agenda.infraestructure.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
