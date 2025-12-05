package com.servicio.reservas.agenda.application.services.reservations;

import com.servicio.reservas.agenda.application.dto.reservations.filters.FilterReservationAdmin;
import com.servicio.reservas.agenda.application.dto.reservations.filters.FilterReservationUser;
import com.servicio.reservas.agenda.application.dto.reservations.RequestReservation;
import com.servicio.reservas.agenda.application.dto.reservations.ResponseReservation;
import java.util.List;

public interface IReservationService {

    ResponseReservation createReservation(RequestReservation reservation);
    ResponseReservation editReservation(Long id, RequestReservation reservation);
    ResponseReservation findReservationById(Long id);
    void cancelReservation(Long id);
    void deactivateReservation(Long id);
    List<ResponseReservation> searchReservationsUser( FilterReservationUser filters);
    List<ResponseReservation> searchAllReservationsAdmin(FilterReservationAdmin filters);
    List<ResponseReservation> getReservationsCompletedForTime(String period);

}
