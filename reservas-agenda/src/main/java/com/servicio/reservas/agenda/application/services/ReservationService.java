package com.servicio.reservas.agenda.application.services;

import com.servicio.reservas.agenda.application.dto.RequestReservation;
import com.servicio.reservas.agenda.application.dto.ReservationMapper;
import com.servicio.reservas.agenda.application.dto.ResponseReservation;
import com.servicio.reservas.agenda.domain.entities.Reservation;
import com.servicio.reservas.agenda.domain.repository.IReservationRepository;
import com.servicio.reservas.agenda.infraestructure.exception.ReservationsException;
import com.servicio.reservas.agenda.infraestructure.services.ServiceDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

        LocalTime duration = serviceDTO.map(ServiceDTO::getDuration)
                .orElseThrow(() -> new IllegalArgumentException("The service duration is empty"));
        LocalTime endTime = reservation.getTimeStart().plusHours(duration.getHour())
                .plusMinutes(duration.getMinute());

        Reservation reservation1 = ReservationMapper.toDomain(reservation, endTime);

        shiftService.validateShift(reservation1);

        Reservation reservation2 = reservationRepository.save(reservation1);

        shiftService.createShift(reservation2);

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
            shiftService.validateShift(ReservationMapper.toDomain(reservation, endTime));

            //edito
            foundReservation.updateReservation(reservation.getDate(), reservation.getTimeStart(), endTime);
            Reservation updatedR = reservationRepository.save(foundReservation);

            return ReservationMapper.toResponse(updatedR);

        }else {
            throw new ReservationsException("The reservation ID " + id + " is deactivated.");
        }

        //validar que no haya una reserva a esa hora que voy a poner
    }

    @Override
    public void cancelReservation(Long id) {

        Reservation foundReservation = findReservationByIdInternal(id);

        LocalDateTime reservationStartime = LocalDateTime.of(foundReservation.getDate(), foundReservation.getTimeStart());
        LocalDateTime today = LocalDateTime.now(); //fecha y hora actual
        LocalDateTime minimumHours = reservationStartime.minusHours(24);

        if(today.isBefore(minimumHours)){

            foundReservation.setStatus("CANCELED");
            foundReservation.setActive(false);
            reservationRepository.save(foundReservation);
            shiftService.deleteShiftFromReservation(id);

        }else{
            throw new ReservationsException("To cancel a reservation, you must give at least 24 hours' notice. ");
        }
    }

    @Override
    public void deactivateReservation(Long id) {

        Reservation foundReservation = findReservationByIdInternal(id);
        foundReservation.setActive(false);

        reservationRepository.save(foundReservation);

    }

    @Override
    public ResponseReservation findReservationById(Long id) {
        return ReservationMapper.toResponse(findReservationByIdInternal(id));
    }

    @Override
    public void deleteReservation(Long id) {
        //eliminar reserva solo si esta status = cancelada o active = false
        Reservation foundReservation = findReservationByIdInternal(id);

        if(!foundReservation.getActive()){}

    }

    private Reservation findReservationByIdInternal(Long id) {
        Reservation reservation = reservationRepository.findByIdReservation(id)
                .orElseThrow(() -> new ReservationsException("Reservation not found"));

        if (!reservation.getActive()) {
            throw new ReservationsException("The reservation ID " + id + " is deactivated.");
        }

        return reservation;
    }
}
