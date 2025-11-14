package com.servicio.reservas.agenda.application.services;

import com.servicio.reservas.agenda.domain.entities.Shift;
import com.servicio.reservas.agenda.infraestructure.services.ServiceDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface IShiftService {
    void validateWorkSchedule(LocalTime start, LocalTime end);
    Optional<ServiceDTO> validationService(Long serviceId);
    void validateShiftDateTime(LocalDate date, LocalTime startTime);
    boolean validateAvailabilityBarberForUpdate(Long barberId, LocalDate date, LocalTime startTime, LocalTime endTime, Long shiftId);
    boolean validateAvailabilityBarber(Long barberId, LocalDate date, LocalTime startTime, LocalTime endTime);
    Shift createShift(Long barberId, LocalDate date, LocalTime startTime, LocalTime endTime);
    void deleteShift(Long id);
    Shift findById(Long id);
    Shift updateShift(Long id, Long barberId, LocalDate date, LocalTime startTime, LocalTime endTime);
    void validateShift(Long barberId, Long serviceId, LocalDate date, LocalTime start, LocalTime end);

    }
