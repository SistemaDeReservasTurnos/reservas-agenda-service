package com.servicio.reservas.agenda.application.services;

import com.servicio.reservas.agenda.application.dto.reservationsFilters.FilterReservationAdmin;
import com.servicio.reservas.agenda.application.dto.reservationsFilters.FilterReservationUser;
import com.servicio.reservas.agenda.application.dto.RequestReservation;
import com.servicio.reservas.agenda.application.dto.ResponseReservation;
import java.util.List;

public interface IReservationService {
    ResponseReservation createReservation(RequestReservation reservation);
    ResponseReservation editReservation(Long id, RequestReservation reservation);
    ResponseReservation findReservationById(Long id);
    void cancelReservation(Long id);
    void deactivateReservation(Long id);
    List<ResponseReservation> searchReservationsUser( FilterReservationUser filters);
    List<ResponseReservation> searchAllReservationsAdmin(FilterReservationAdmin filters);


}
