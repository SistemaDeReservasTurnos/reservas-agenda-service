package com.servicio.reservas.agenda.application.dto.reservations.filters;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter

public class FilterReservationAdmin {

    private Long userId;
    private Long serviceId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

}
