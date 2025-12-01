package com.servicio.reservas.agenda.application.services.shifts;

import com.servicio.reservas.agenda.application.AvailabilityMode;
import com.servicio.reservas.agenda.domain.TimeRange;
import com.servicio.reservas.agenda.domain.entities.Reservation;
import com.servicio.reservas.agenda.domain.entities.Shift;
import com.servicio.reservas.agenda.domain.repository.IShiftRepository;
import com.servicio.reservas.agenda.infraestructure.exception.BusinessException;
import com.servicio.reservas.agenda.infraestructure.exception.ResourceNotFoundException;
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
                    throw new BusinessException("The schedule must be between 8:00–12:00 or 14:00–18:00.");
                }
    }

    @Override
    public void validateShiftDateTime(LocalDate date, LocalTime startTime){
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        if(date.isBefore(today)) {
            throw new BusinessException("You cannot schedule a shift in the past.");
        }

        if(date.isEqual(today) && startTime.isBefore(now)) {
            throw new BusinessException("The selected time is no longer available today.");
        }
    }

    @Override
    public boolean validateAvailabilityBarber(Reservation reservation, AvailabilityMode mode) {

        boolean existsOverlap = switch (mode) {
            case UPDATE -> shiftRepository.existsOverlappingReservationUpdate(reservation);
            case CREATE -> shiftRepository.existsOverlappingReservationCreate(reservation);
        };

        if (existsOverlap) {
            throw new BusinessException("The selected time overlaps with another shift");
        }

        return true;
    }

    @Override
    public Optional<ServiceDTO> validationService(Long serviceId) {
        try {
            return serviceClient.findServiceById(serviceId);
        } catch (feign.FeignException.NotFound e) {
            throw new ResourceNotFoundException("The service with ID " + serviceId + " was not found.");
        }
    }

    @Override
    public Shift createShift(Reservation reservation){
        Shift shift = new Shift();
        shift.setBarberId(reservation.getBarberId());
        shift.setDate(reservation.getDate());
        shift.setTimeStart(reservation.getTimeStart());
        shift.setTimeEnd(reservation.getTimeEnd());
        shift.setReservationId(reservation.getId());
        return shiftRepository.save(shift);
    }

    @Override
    public void deleteShift(Long id){
        shiftRepository.deleteById(id);
    }

    @Override
    public void validateShift(Reservation reservation , AvailabilityMode mode){
        validateShiftDateTime(reservation.getDate(), reservation.getTimeStart());
        validateWorkSchedule(reservation.getTimeStart(), reservation.getTimeEnd());
        validationService(reservation.getServiceId());
        validateAvailabilityBarber(reservation, mode);
    }

    @Override
    public Shift updateShift(Reservation reservation) {
        Shift existingShift = findById(reservation.getId());

        existingShift.setBarberId(reservation.getBarberId());
        existingShift.setDate(reservation.getDate());
        existingShift.setTimeStart(reservation.getTimeStart());
        existingShift.setTimeEnd(reservation.getTimeEnd());

        return shiftRepository.save(existingShift);
    }

    public Shift findById(Long id) {
        return shiftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with id: " + id));
    }

    @Override
    public void deleteShiftFromReservation(Long reservationId){
        shiftRepository.deleteShiftFromReservation(reservationId);
    }


}