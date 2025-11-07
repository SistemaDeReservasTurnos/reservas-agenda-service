package com.servicio.reservas.agenda.infraestructure.exception;

public class CustomException extends  RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
