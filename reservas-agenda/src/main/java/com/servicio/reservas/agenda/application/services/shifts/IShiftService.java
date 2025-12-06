package com.servicio.reservas.agenda.application.services.shifts;

import com.servicio.reservas.agenda.domain.entities.AvailabilityMode;
import com.servicio.reservas.agenda.domain.entities.Reservation;
import com.servicio.reservas.agenda.domain.entities.Shift;
import com.servicio.reservas.agenda.infraestructure.output.client.services.ServiceDTO;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface IShiftService {

    void validateWorkSchedule(LocalTime start, LocalTime end);
    Optional<ServiceDTO> validationService(Long serviceId);
    void validateShiftDateTime(LocalDate date, LocalTime startTime);
    boolean validateAvailabilityBarber(Reservation reservation, AvailabilityMode mode);
    Shift createShift(Reservation reservation);
    void deleteShift(Long id);
    Shift findById(Long id);
    Shift updateShift(Reservation reservation);
    void validateShift(Reservation reservation, AvailabilityMode mode);
    void deleteShiftFromReservation(Long reservationId);

}
