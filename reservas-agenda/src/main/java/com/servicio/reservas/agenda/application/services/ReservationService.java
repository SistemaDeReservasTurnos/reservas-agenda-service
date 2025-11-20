package com.servicio.reservas.agenda.application.services;

import com.servicio.reservas.agenda.application.dto.*;
import com.servicio.reservas.agenda.domain.entities.Reservation;
import com.servicio.reservas.agenda.domain.repository.IReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService implements IReservationService {

    private final IReservationRepository reservationRepository;

    public ReservationService(IReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public ResponseReservation createReservation(RequestReservation request) {

        var endTime = request.getTimeStart().plusMinutes(30);

        Reservation reservation = ReservationMapper.toDomain(request, endTime);
        Reservation saved = reservationRepository.save(reservation);

        return ReservationMapper.toResponse(saved);
    }

    @Override
    public ResponseReservation editReservation(Long id, RequestReservation request) {
        Reservation reservation = reservationRepository.findByIdReservation(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.updateReservation(
                request.getDate(),
                request.getTimeStart(),
                request.getTimeStart().plusMinutes(30)
        );

        Reservation updated = reservationRepository.save(reservation);
        return ReservationMapper.toResponse(updated);
    }

    @Override
    public ResponseReservation findReservationById(Long id) {
        Reservation reservation = reservationRepository.findByIdReservation(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        return ReservationMapper.toResponse(reservation);
    }

    @Override
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findByIdReservation(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setActive(false);
        reservationRepository.save(reservation);
    }

    @Override
    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findByIdReservation(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setStatus("CANCELLED");
        reservationRepository.save(reservation);
    }

    @Override
    public void deactivateReservation(Long id) {
        Reservation reservation = reservationRepository.findByIdReservation(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setActive(false);
        reservationRepository.save(reservation);
    }



    @Override
    public List<ResponseReservation> searchAllReservations(FilterReservationAdmin filters) {
        List<Reservation> list = reservationRepository.adminSearchReservations(filters);
        return list.stream().map(ReservationMapper::toResponse).toList();
    }

}



