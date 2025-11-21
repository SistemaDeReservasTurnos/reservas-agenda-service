package com.servicio.reservas.agenda.application.dto.reservationsFilters;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter

public class FilterReservationAdmin {

    private Long userId;
    private Long serviceId;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
}
