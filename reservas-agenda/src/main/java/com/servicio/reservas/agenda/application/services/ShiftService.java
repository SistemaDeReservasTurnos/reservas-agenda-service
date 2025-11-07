package com.servicio.reservas.agenda.application.services;

import com.servicio.reservas.agenda.domain.TimeRange;
import com.servicio.reservas.agenda.infraestructure.exception.CustomException;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class ShiftService implements IShiftService {

    private static final List<TimeRange> WORK_SCHEDULES = List.of(
            new TimeRange(LocalTime.of(8,0), LocalTime.of(12,0)),
            new TimeRange(LocalTime.of(14,0), LocalTime.of(18,0))
    );

    @Override
    public void shiftValidation(LocalTime start, LocalTime end){
        boolean isValid = WORK_SCHEDULES.stream().anyMatch(
                range -> range.isContained(start, end));

                if(!isValid){
                    throw new CustomException("The schedule must be between 8:00–12:00 or 14:00–18:00.");
                }
    };

}
