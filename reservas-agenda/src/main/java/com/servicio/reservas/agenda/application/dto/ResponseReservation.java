package com.servicio.reservas.agenda.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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
    private Double amount;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private String status;
    private Boolean active;

}
