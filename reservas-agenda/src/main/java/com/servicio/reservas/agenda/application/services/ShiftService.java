package com.servicio.reservas.agenda.application.services;

import com.servicio.reservas.agenda.domain.TimeRange;
import com.servicio.reservas.agenda.domain.entities.Shift;
import com.servicio.reservas.agenda.domain.repository.IShiftRepository;
import com.servicio.reservas.agenda.infraestructure.exception.CustomException;
import com.servicio.reservas.agenda.infraestructure.services.ServiceClient;
import com.servicio.reservas.agenda.infraestructure.services.ServiceDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ShiftService implements IShiftService {

    private static final List<TimeRange> WORK_SCHEDULES = List.of(
            new TimeRange(LocalTime.of(8,0), LocalTime.of(12,0)),
            new TimeRange(LocalTime.of(14,0), LocalTime.of(18,0))
    );

    private final ServiceClient serviceClient;
    private final IShiftRepository shiftRepository;

    public ShiftService(ServiceClient serviceClient, IShiftRepository shiftRepository) {
        this.serviceClient = serviceClient;
        this.shiftRepository = shiftRepository;
    }

    @Override
    public void validateWorkSchedule(LocalTime start, LocalTime end){
        boolean isValid = WORK_SCHEDULES.stream().anyMatch(
                range -> range.isContained(start, end));

                if(!isValid){
                    throw new CustomException("The schedule must be between 8:00–12:00 or 14:00–18:00.");
                }
    }

    @Override
    public void validateShiftDateTime(LocalDate date, LocalTime startTime){
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        if(date.isBefore(today)) {
            throw new CustomException("You cannot schedule a shift in the past.");
        }

        if(date.isEqual(today) && startTime.isBefore(now)) {
            throw new CustomException("The selected time is no longer available today.");
        }
    }

    @Override
    public boolean validateAvailabilityBarberForUpdate(Long barberId, LocalDate date, LocalTime startTime, LocalTime endTime, Long shiftId) {
        if (barberId == null) {
            throw new CustomException("The field barberId cannot be null");
        }
        if (date == null) {
            throw new CustomException("The field date cannot be null");
        }
        if (startTime == null || endTime == null) {
            throw new CustomException("Start time and end time must be provided");
        }
        boolean existsOverlap = shiftRepository.existsOverlappingReservationUpdate(barberId, date, startTime, endTime, shiftId);
        if (existsOverlap) {
            throw new CustomException("The selected time overlaps with another shift");
        }
        return true;
    }

    @Override
    public boolean validateAvailabilityBarber(Long barberId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        if (barberId == null) {
            throw new CustomException("The field barberId cannot be null");
        }
        if (date == null) {
            throw new CustomException("The field date cannot be null");
        }
        if (startTime == null || endTime == null) {
            throw new CustomException("Start time and end time must be provided");
        }
        boolean existsOverlap = shiftRepository.existsOverlappingReservationCreate(barberId, date, startTime, endTime);
        if (existsOverlap) {
            throw new CustomException("The selected time overlaps with another shift");
        }
        return true;
    }

    @Override
    public Optional<ServiceDTO> validationService(Long serviceId) {
        try {
            return serviceClient.findServiceById(serviceId);
        } catch (feign.FeignException.NotFound e) {
            throw new CustomException("The service with ID " + serviceId + " was not found.");
        }
    }

    @Override
    public Shift createShift(Long barberId, LocalDate date, LocalTime startTime, LocalTime endTime){
        Shift shift = new Shift();
        shift.setBarberId(barberId);
        shift.setDate(date);
        shift.setTimeStart(startTime);
        shift.setTimeEnd(endTime);
        return shiftRepository.save(shift);
    }

    @Override
    public void deleteShift(Long id){
        shiftRepository.deleteById(id);
    }

    @Override
    public void validateShift(Long barberId, Long serviceId, LocalDate date, LocalTime start, LocalTime end){
        validateShiftDateTime(date, start);
        validateWorkSchedule(start, end);
        validationService(serviceId);
        validateAvailabilityBarber(barberId, date, start, end);
    }

    @Override
    public Shift updateShift(Long id, Long barberId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        Shift existingShift = findById(id);

        existingShift.setBarberId(barberId);
        existingShift.setDate(date);
        existingShift.setTimeStart(startTime);
        existingShift.setTimeEnd(endTime);

        return shiftRepository.save(existingShift);
    }

    public Shift findById(Long id) {
        return shiftRepository.findById(id)
                .orElseThrow(() -> new CustomException("Shift not found with id: " + id));
    }


}