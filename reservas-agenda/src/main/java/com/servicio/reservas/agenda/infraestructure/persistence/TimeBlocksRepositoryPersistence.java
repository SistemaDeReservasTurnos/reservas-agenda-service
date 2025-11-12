package com.servicio.reservas.agenda.infraestructure.persistence;

import com.servicio.reservas.agenda.domain.entities.Shift;
import com.servicio.reservas.agenda.domain.repository.ITimeBlocksRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TimeBlocksRepositoryPersistence implements ITimeBlocksRepository {

    private final SpringShiftRepository springShiftRepository;

    public TimeBlocksRepositoryPersistence(SpringShiftRepository springShiftRepository) {
        this.springShiftRepository = springShiftRepository;
    }

    @Override
    public boolean existsOverlappingReservation(Long barberId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        return springShiftRepository.existsOverlappingReservation(barberId, date, startTime, endTime);
    }

    @Override
    public Shift save(Shift shift) {
        ShiftModel model = ShiftMapper.toModel(shift);
        ShiftModel saved = springShiftRepository.save(model);
        return ShiftMapper.toDomain(saved);
    }

    @Override
    public List<Shift> findByBarberAndDate(Long barberId, LocalDate date) {
        List<ShiftModel> models = springShiftRepository.findByBarberIdAndDate(barberId, date);
        return models.stream()
                .map(ShiftMapper::toDomain)
                .collect(Collectors.toList());
    }
}
