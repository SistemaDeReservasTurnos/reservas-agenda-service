package com.servicio.reservas.agenda.application.services;

import com.servicio.reservas.agenda.infraestructure.services.ServiceDTO;

import java.time.LocalTime;
import java.util.Optional;

public interface IShiftService {
    void shiftValidation(LocalTime start, LocalTime end);
    Optional<ServiceDTO> validationService(Long serviceId);
}
