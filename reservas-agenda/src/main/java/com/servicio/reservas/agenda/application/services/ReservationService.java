package com.servicio.reservas.agenda.application.services;

import com.servicio.reservas.agenda.application.dto.RequestReservation;
import com.servicio.reservas.agenda.application.dto.ReservationMapper;
import com.servicio.reservas.agenda.application.dto.ResponseReservation;
import com.servicio.reservas.agenda.domain.entities.Reservation;
import com.servicio.reservas.agenda.domain.repository.IReservationRepository;
import com.servicio.reservas.agenda.infraestructure.exception.ReservationsNoActiveException;
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

        LocalTime duration = serviceDTO.map(ServiceDTO::getDuration)
                .orElseThrow(() -> new IllegalArgumentException("The service duration is empty"));
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

    @Override
    public ResponseReservation editReservation(Long id, RequestReservation reservation) {

        Optional<ServiceDTO> serviceDTO = shiftService.validationService(reservation.getServiceId());

        Reservation foundReservation = findReservationByIdInternal(id); //busco la reserva a editar


        //modifico la reserva si active == true
        if(foundReservation.getActive()){

            LocalTime duration = serviceDTO.map(ServiceDTO::getDuration)
                    .orElseThrow(() -> new IllegalArgumentException("The service duration is empty"));

            LocalTime endTime = reservation.getTimeStart().plusHours(duration.getHour()).plusMinutes(duration.getMinute());

            //valido el turno antes de modificar la reserva
            shiftService.validateShift(reservation.getBarberId(), reservation.getServiceId(), reservation.getDate(), reservation.getTimeStart(), endTime);

            //edito
            foundReservation.updateReservation(reservation.getDate(), reservation.getTimeStart(), endTime);
            Reservation updatedR = reservationRepository.save(foundReservation);

            return ReservationMapper.toResponse(updatedR);

        }else {
            throw new ReservationsNoActiveException(id);
        }

        //validar que no haya una reserva a esa hora que voy a poner
    }

    @Override
    public ResponseReservation findReservationById(Long id) {
        return ReservationMapper.toResponse(findReservationByIdInternal(id));
    }

    private Reservation findReservationByIdInternal(Long id) {
        return reservationRepository.findByIdReservation(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }
}
