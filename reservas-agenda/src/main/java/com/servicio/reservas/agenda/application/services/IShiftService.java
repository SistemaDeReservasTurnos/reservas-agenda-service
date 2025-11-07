package com.servicio.reservas.agenda.application.services;

import java.time.LocalTime;

public interface IShiftService {
    void shiftValidation(LocalTime start, LocalTime end);
}
