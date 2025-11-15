package com.servicio.reservas.agenda.application.services;

import com.servicio.reservas.agenda.application.dto.RequestReservation;
import com.servicio.reservas.agenda.application.dto.ReservationMapper;
import com.servicio.reservas.agenda.application.dto.ResponseReservation;
import com.servicio.reservas.agenda.domain.entities.Reservation;
import com.servicio.reservas.agenda.domain.repository.IReservationRepository;
import com.servicio.reservas.agenda.infraestructure.services.ServiceDTO;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;

@Service
public class ReservationService implements IReservationService {

    private final IShiftService shiftService;
    private final IReservationRepository  reservationRepository;

    public ReservationService(IShiftService shiftService, IReservationRepository reservationRepository) {
        this.shiftService = shiftService;
        this.reservationRepository = reservationRepository;
    }


    @Override
    public ResponseReservation createReservation(RequestReservation reservation) {

        Optional<ServiceDTO> serviceDTO = shiftService.validationService(reservation.getServiceId());
        Reservation reservation1 = new Reservation();

        LocalTime duration = serviceDTO.get().getDuration();
        LocalTime endTime = reservation.getTimeStart().plusHours(duration.getHour())
                .plusMinutes(duration.getMinute());
        reservation1.setServiceId(reservation.getServiceId());
        reservation1.setUserId(reservation.getUserId());
        reservation1.setDate(reservation.getDate());
        reservation1.setBarberId(reservation.getBarberId());
        reservation1.setTimeStart(reservation.getTimeStart());
        reservation1.setTimeEnd(endTime);
        //reservation1.setStatus();
        //reservation1.setActive();

        shiftService.validateShift(reservation.getBarberId(), reservation.getServiceId(), reservation.getDate(), reservation.getTimeStart(), endTime);
        shiftService.createShift(reservation.getBarberId(), reservation.getDate(), reservation.getTimeStart(), endTime);

        Reservation reservation2 = reservationRepository.save(reservation1);

        return ReservationMapper.toResponse(reservation2);
    }
}
