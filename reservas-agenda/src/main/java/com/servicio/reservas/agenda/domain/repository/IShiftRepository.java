package com.servicio.reservas.agenda.domain.repository;

import com.servicio.reservas.agenda.domain.entities.Shift;
import java.time.LocalDate;
import java.util.List;

public interface IShiftRepository {
    List<Shift> findAvailableByBarberAndDate(Long barberId, LocalDate date);
}
