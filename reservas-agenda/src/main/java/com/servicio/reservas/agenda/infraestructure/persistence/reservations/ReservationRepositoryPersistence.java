package com.servicio.reservas.agenda.infraestructure.persistence.reservations;

import com.servicio.reservas.agenda.domain.entities.Reservation;
import com.servicio.reservas.agenda.domain.repository.IReservationRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepositoryPersistence implements IReservationRepository {

    private final SpringReservationRepository  springReservationRepository;
    public ReservationRepositoryPersistence(SpringReservationRepository springReservationRepository) {
        this.springReservationRepository = springReservationRepository;
    }


    @Override
    public Reservation save(Reservation reservation) {

        ReservationModel reservationModel = ReservationModelMapper.toModel(reservation);
        ReservationModel savedReservationModel = springReservationRepository.save(reservationModel);
        return ReservationModelMapper.toDomain(savedReservationModel);
    }

    @Override
    public Optional<Reservation> findByIdReservation(Long id) {

        return springReservationRepository.findById(id).map(ReservationModelMapper::toDomain);

    }

    @Override
    public List<Reservation> findAllActiveThatEnded(LocalTime now, LocalDate today){

        List<ReservationModel> models = springReservationRepository.findAllActiveThatEnded(now, today);
        return models.stream()
                .map(ReservationModelMapper::toDomain)
                .toList();
    }
}
