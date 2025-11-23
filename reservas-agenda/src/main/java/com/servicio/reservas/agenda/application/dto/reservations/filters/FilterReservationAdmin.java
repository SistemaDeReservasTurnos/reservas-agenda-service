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

    // Default constructor
    public FilterReservationAdmin() {
    }

    // Parameterized constructor
    public FilterReservationAdmin(Long userId, Long serviceId, LocalDate startDate, LocalDate endDate, String status) {
        this.userId = userId;
        this.serviceId = serviceId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }
}
