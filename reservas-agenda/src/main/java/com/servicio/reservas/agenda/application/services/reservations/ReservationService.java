package com.servicio.reservas.agenda.application.services.reservations;

import com.servicio.reservas.agenda.domain.entities.AvailabilityMode;
import com.servicio.reservas.agenda.application.dto.reservations.*;
import com.servicio.reservas.agenda.application.dto.reservations.filters.FilterReservationAdmin;
import com.servicio.reservas.agenda.application.dto.reservations.filters.FilterReservationUser;
import com.servicio.reservas.agenda.application.services.shifts.IShiftService;
import com.servicio.reservas.agenda.domain.entities.Reservation;
import com.servicio.reservas.agenda.domain.repository.IReservationRepository;
import com.servicio.reservas.agenda.infraestructure.output.messaging.EventPublisher;
import com.servicio.reservas.agenda.infraestructure.exception.BusinessException;
import com.servicio.reservas.agenda.infraestructure.exception.ResourceNotFoundException;
import com.servicio.reservas.agenda.infraestructure.output.client.services.ServiceClient;
import com.servicio.reservas.agenda.infraestructure.output.client.services.ServiceDTO;
import com.servicio.reservas.agenda.infraestructure.output.client.users.UserClient;
import com.servicio.reservas.agenda.infraestructure.output.client.users.UserDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService implements IReservationService {

    private final IShiftService shiftService;
    private final IReservationRepository  reservationRepository;
    private final UserClient userClient;
    private final ServiceClient serviceClient;
    private final EventPublisher eventPublisher;

    public ReservationService(IShiftService shiftService, IReservationRepository reservationRepository, UserClient userClient, ServiceClient serviceClient, EventPublisher eventoPublisher) {
        this.shiftService = shiftService;
        this.reservationRepository = reservationRepository;
        this.userClient = userClient;
        this.serviceClient = serviceClient;
        this.eventPublisher = eventoPublisher;
    }

    @Override
    public ResponseReservation createReservation(RequestReservation reservation) {

        Optional<ServiceDTO> serviceDTO = shiftService.validationService(reservation.getServiceId());

        LocalTime duration = serviceDTO.map(ServiceDTO::getDuration)
                .orElseThrow(() -> new BusinessException("The service duration is empty"));
        LocalTime endTime = reservation.getTimeStart().plusHours(duration.getHour())
                .plusMinutes(duration.getMinute());

        Double amount = serviceDTO.map(ServiceDTO::getPrice)
                .orElseThrow(() -> new BusinessException("The price is empty"));

        Reservation newReservation = ReservationMapper.toDomain(reservation, endTime, amount);

        shiftService.validateShift(newReservation, AvailabilityMode.CREATE);

        Reservation reservation2 = reservationRepository.save(newReservation);

        shiftService.createShift(reservation2);

        ResponseReservation responseReservation = buildResponseWithUserNames(reservation2);

        // Sending created reservation to reporting
        //ReportReservationEvent event = ReservationMapper.toReport(responseReservation);
        //eventPublisher.publishEvent("reserva.creada", event);

        return responseReservation;
    }

    @Override
    public ResponseReservation editReservation(Long id, RequestReservation reservation) {

        Optional<ServiceDTO> serviceDTO = shiftService.validationService(reservation.getServiceId());

        Reservation foundReservation = findReservationByIdInternal(id);

        //I modify the reservation if active == true
        if(foundReservation.getActive()){

            LocalTime duration = serviceDTO.map(ServiceDTO::getDuration)
                    .orElseThrow(() -> new IllegalArgumentException("The service duration is empty"));

            LocalTime endTime = reservation.getTimeStart().plusHours(duration.getHour()).plusMinutes(duration.getMinute());

            Double amount = serviceDTO.map(ServiceDTO::getPrice)
                    .orElseThrow(() -> new IllegalArgumentException("The price is empty"));

            //Shift validated before modifying the reservation
            shiftService.validateShift(ReservationMapper.toDomain(reservation, endTime, amount),  AvailabilityMode.UPDATE);

            //edit
            foundReservation.updateReservation(reservation.getDate(), reservation.getTimeStart(), endTime);
            Reservation updatedR = reservationRepository.save(foundReservation);

            return buildResponseWithUserNames(updatedR);

        }else {
            throw new BusinessException("The reservation ID " + id + " is deactivated.");
        }

    }

    @Override
    public void cancelReservation(Long id) {

        Reservation foundReservation = findReservationByIdInternal(id);

        LocalDateTime reservationStartime = LocalDateTime.of(foundReservation.getDate(), foundReservation.getTimeStart());
        LocalDateTime today = LocalDateTime.now(); //fecha y hora actual
        LocalDateTime minimumHours = reservationStartime.minusHours(24);

        if (today.isBefore(minimumHours)) {

            foundReservation.setStatus("CANCELED");
            foundReservation.setActive(false);
            reservationRepository.save(foundReservation);
            shiftService.deleteShiftFromReservation(id);

            //CancelReservationEvent event = new CancelReservationEvent();
            //event.setReservationId(foundReservation.getId());
            //eventPublisher.publishEvent("reserva.cancelada", event);

        } else {
            throw new BusinessException("To cancel a reservation, you must give at least 24 hours' notice. ");
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

        // Convert to DTO
        return results.stream().map(this::buildResponseWithUserNames).toList();
    }

    @Override
    public List<ResponseReservation> searchAllReservationsAdmin(FilterReservationAdmin filters) {

        List<Reservation> list = reservationRepository.adminSearchReservations(filters);
        return list.stream().map(this::buildResponseWithUserNames).toList();
    }

    @Override
    public ResponseReservation findReservationById(Long id) {

        Reservation reservation = findReservationByIdInternal(id);
        return buildResponseWithUserNames(reservation);
    }

    private Reservation findReservationByIdInternal(Long id) {

        Reservation reservation = reservationRepository.findByIdReservation(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));

        if (!reservation.getActive()) {
            throw new BusinessException("The reservation ID " + id + " is deactivated.");
        }
        return reservation;
    }

    private ResponseReservation buildResponseWithUserNames(Reservation reservation) {

        UserDTO client = userClient.findUserById(reservation.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("The client is not found."));

        UserDTO barber = userClient.findUserById(reservation.getBarberId())
                .orElseThrow(() -> new ResourceNotFoundException("The barber is not found."));

        ServiceDTO service = serviceClient.findServiceById(reservation.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("The service is not found."));

        return ReservationMapper.toResponse(
                reservation,
                client.getName(),
                barber.getName(),
                service.getName()
        );
    }
}
