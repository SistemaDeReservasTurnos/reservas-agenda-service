package com.servicio.reservas.agenda.domain.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shift {

    private Long id;
    private Long barberId;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private String state;
}
