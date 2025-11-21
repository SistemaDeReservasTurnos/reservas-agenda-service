package com.servicio.reservas.agenda.application.dto.reservationsFilters;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class FilterReservationUser {

    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    public FilterReservationUser() {}

    public FilterReservationUser(Long userId, LocalDate startDate, LocalDate endDate, String status) {
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }
}

