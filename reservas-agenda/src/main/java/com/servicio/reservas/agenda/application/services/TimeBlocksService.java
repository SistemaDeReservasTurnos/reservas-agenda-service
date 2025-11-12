package com.servicio.reservas.agenda.application.services;

import com.servicio.reservas.agenda.domain.repository.ITimeBlocksRepository;
import com.servicio.reservas.agenda.infraestructure.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class TimeBlocksService implements ITimeBlocksService {

    private final ITimeBlocksRepository timeBlocksRepository;

    @Override
    public boolean validate(Long barberId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        if (barberId == null) {
            throw new CustomException("The field barberId cannot be null");
        }
        if (date == null) {
            throw new CustomException("The field date cannot be null");
        }
        if (startTime == null || endTime == null) {
            throw new CustomException("Start time and end time must be provided");
        }
        boolean existsOverlap = timeBlocksRepository.existsOverlappingReservation(barberId, date, startTime, endTime);
        if (existsOverlap) {
            throw new CustomException("The selected time overlaps with another shift");
        }
        return true;
    }
}