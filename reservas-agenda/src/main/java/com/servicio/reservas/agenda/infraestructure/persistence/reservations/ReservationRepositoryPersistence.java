package com.servicio.reservas.agenda.infraestructure.persistence.reservations;

import com.servicio.reservas.agenda.application.dto.reservations.filters.FilterReservationAdmin;
import com.servicio.reservas.agenda.domain.entities.Reservation;
import com.servicio.reservas.agenda.domain.repository.IReservationRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepositoryPersistence implements IReservationRepository {

    private final SpringReservationRepository springReservationRepository;

    public ReservationRepositoryPersistence(SpringReservationRepository springReservationRepository) {
        this.springReservationRepository = springReservationRepository;
    }

    @Override
    public Reservation save(Reservation reservation) {
        ReservationModel model = ReservationModelMapper.toModel(reservation);
        ReservationModel saved = springReservationRepository.save(model);
        return ReservationModelMapper.toDomain(saved);
    }

    @Override
    public Optional<Reservation> findByIdReservation(Long id) {
        return springReservationRepository.findById(id)
                .map(ReservationModelMapper::toDomain);
    }

    @Override
    public List<Reservation> findAllActiveThatEnded(LocalTime now, LocalDate today) {
        return springReservationRepository.findAllActiveThatEnded(now, today)
                .stream()
                .map(ReservationModelMapper::toDomain)
                .toList();
    }

    @Override
    public List<Reservation> userReservations(Long userId, LocalDate startDate, LocalDate endDate, String status) {
        return springReservationRepository.searchByFilters(userId, startDate, endDate, status)
                .stream()
                .map(ReservationModelMapper::toDomain)
                .toList();
    }

    @Override
    public List<Reservation> adminSearchReservations(FilterReservationAdmin filters) {

        List<ReservationModel> result =
                springReservationRepository.searchAdminFilters(
                        filters.getUserId(),
                        filters.getServiceId(),
                        filters.getStartDate(),
                        filters.getEndDate(),
                        filters.getStatus());

        return result.stream().map(ReservationModelMapper::toDomain).toList();
    }

    @Override
    public List<Reservation> findCompletedByDate(LocalDate startDate) {
        List<ReservationModel> reservations = springReservationRepository.findCompletedStatusByDate(startDate);
        return reservations.stream()
                .map(ReservationModelMapper::toDomain)
                .toList();
    }
}
