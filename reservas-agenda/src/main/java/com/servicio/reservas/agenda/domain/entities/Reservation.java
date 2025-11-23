package com.servicio.reservas.agenda.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Reservation {

    private Long id;
    private Long serviceId;
    private Long userId;
    private Long barberId;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private String status;
    private Boolean active;
    private Double amount;

    public void updateReservation(LocalDate date, LocalTime timeStart, LocalTime timeEnd) {
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;

    }
}
