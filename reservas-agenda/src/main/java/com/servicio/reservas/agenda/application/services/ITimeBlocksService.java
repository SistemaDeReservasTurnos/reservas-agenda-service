package com.servicio.reservas.agenda.application.services;

import java.time.LocalDate;
import java.time.LocalTime;
public interface ITimeBlocksService {
    boolean validate(Long barberId, LocalDate date, LocalTime startTime, LocalTime endTime);
}
