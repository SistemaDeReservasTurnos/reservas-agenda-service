package com.servicio.reservas.agenda.domain.repository;

import com.servicio.reservas.agenda.domain.entities.Shift;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ITimeBlocksRepository {

    boolean existsOverlappingReservation(Long barberId, LocalDate date, LocalTime startTime, LocalTime endTime);

    Shift save(Shift shift);

    List<Shift> findByBarberAndDate(Long barberId, LocalDate date);
}
