package com.servicio.reservas.agenda.application.services.reservations;

import com.servicio.reservas.agenda.application.AvailabilityMode;
import com.servicio.reservas.agenda.application.dto.reservations.filters.FilterReservationAdmin;
import com.servicio.reservas.agenda.application.dto.reservations.filters.FilterReservationUser;
import com.servicio.reservas.agenda.application.dto.reservations.RequestReservation;
import com.servicio.reservas.agenda.application.dto.reservations.ReservationMapper;
import com.servicio.reservas.agenda.application.dto.reservations.ResponseReservation;
import com.servicio.reservas.agenda.application.services.shifts.IShiftService;
import com.servicio.reservas.agenda.domain.entities.Reservation;
import com.servicio.reservas.agenda.domain.repository.IReservationRepository;
import com.servicio.reservas.agenda.infraestructure.exception.ReservationsException;
import com.servicio.reservas.agenda.infraestructure.services.ServiceDTO;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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

        Double amount = serviceDTO.map(ServiceDTO::getPrice)
                .orElseThrow(() -> new IllegalArgumentException("The price is empty"));

        Reservation newReservation = ReservationMapper.toDomain(reservation, endTime, amount);

        shiftService.validateShift(newReservation, AvailabilityMode.CREATE);

        Reservation reservation2 = reservationRepository.save(newReservation);

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

            Double amount = serviceDTO.map(ServiceDTO::getPrice)
                    .orElseThrow(() -> new IllegalArgumentException("The price is empty"));

            //valido el turno antes de modificar la reserva
            shiftService.validateShift(ReservationMapper.toDomain(reservation, endTime, amount),  AvailabilityMode.UPDATE);

            //edito
            foundReservation.updateReservation(reservation.getDate(), reservation.getTimeStart(), endTime);
            Reservation updatedR = reservationRepository.save(foundReservation);

            return ReservationMapper.toResponse(updatedR);

        }else {
            throw new ReservationsException("The reservation ID " + id + " is deactivated.");
        }

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
        foundReservation.setStatus("DEACTIVATED");

        reservationRepository.save(foundReservation);
        shiftService.deleteShiftFromReservation(id);

    }

    @Override
    public List<ResponseReservation> searchReservationsUser(FilterReservationUser filters) {

        // Llama al repository con los filtros ya organizados
        List<Reservation> results = reservationRepository.userReservations(
                filters.getUserId(),
                filters.getStartDate(),
                filters.getEndDate(),
                filters.getStatus()
        );
        if(results.isEmpty()){ throw new ReservationsException("No reservations found.");}

        // Convertir a DTO
        return results.stream()
                .map(ReservationMapper::toResponse)
                .toList();
    }

    @Override
    public List<ResponseReservation> searchAllReservationsAdmin(FilterReservationAdmin filters) {

        List<Reservation> list = reservationRepository.adminSearchReservations(filters);

        if(list.isEmpty()){ throw new ReservationsException("No reservations found.");}

        return list.stream().map(ReservationMapper::toResponse).toList();
    }

    @Override
    public ResponseReservation findReservationById(Long id) {

        return ReservationMapper.toResponse(findReservationByIdInternal(id));
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
