package com.servicio.reservas.agenda.application.dto.reservations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ResponseReservation {

    private Long id;
    private Long serviceId;
    private Long userId;
    private Long barberId;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private String status;
    private Boolean active;

}
