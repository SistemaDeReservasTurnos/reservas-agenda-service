package com.servicio.reservas.agenda.domain.repository;

import com.servicio.reservas.agenda.domain.entities.Reservation;
import com.servicio.reservas.agenda.domain.entities.Shift;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IShiftRepository {

    List<Shift> findByBarberAndDate(Long barberId, LocalDate date);
    boolean existsOverlappingReservationUpdate(Reservation reservation);
    boolean existsOverlappingReservationCreate(Reservation reservation);
    Shift save(Shift shift);
    void deleteById(Long id);
    Optional<Shift> findById(Long id);
    void updateStateShiftByReservationId(Long id);
    void deleteShiftFromReservation(Long id);

}
