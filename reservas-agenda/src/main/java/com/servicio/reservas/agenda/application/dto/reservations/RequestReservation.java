package com.servicio.reservas.agenda.application.dto.reservations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RequestReservation {

    @NotNull(message = "The id service is required")
    private Long serviceId;
    @NotNull(message = "The user ID is required")
    private Long userId;
    @NotNull(message = "The barber ID is required")
    private Long barberId;
    @NotNull(message = "The date is required")
    private LocalDate date;
    @NotNull(message = "The time start is required")
    private LocalTime timeStart;

}
