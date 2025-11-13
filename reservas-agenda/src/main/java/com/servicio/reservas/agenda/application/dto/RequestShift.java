package com.servicio.reservas.agenda.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestShift{
    private Long barberId;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
}
