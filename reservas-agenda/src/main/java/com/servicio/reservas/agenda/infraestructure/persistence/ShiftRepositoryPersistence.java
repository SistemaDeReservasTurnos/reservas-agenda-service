package com.servicio.reservas.agenda.infraestructure.persistence;


import com.servicio.reservas.agenda.domain.entities.Shift;
import com.servicio.reservas.agenda.domain.repository.IShiftRepository;
import com.servicio.reservas.agenda.infraestructure.exception.CustomException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Component
public class ShiftRepositoryPersistence implements IShiftRepository {

    private final SpringShiftRepository springShiftRepository;

    public ShiftRepositoryPersistence(SpringShiftRepository springShiftRepository) {
        this.springShiftRepository = springShiftRepository;
    }

    @Override
    public boolean existsOverlappingReservationUpdate(Long barberId, LocalDate date, LocalTime startTime, LocalTime endTime, Long id) {
        return springShiftRepository.existsOverlappingReservationUpdate(barberId, date, startTime, endTime,id);
    }

    @Override
    public boolean existsOverlappingReservationCreate(Long barberId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        return springShiftRepository.existsOverlappingReservationCreate(barberId, date, startTime, endTime);
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

    @Override
    public void deleteById(Long id){
        ShiftModel model = springShiftRepository.findById(id)
                .orElseThrow(() ->  new CustomException("Shift not found"));
        springShiftRepository.deleteById(id);
    }

    @Override
    public Optional<Shift> findById(Long id){
        return springShiftRepository.findById(id).map(ShiftMapper::toDomain);
    }

}
