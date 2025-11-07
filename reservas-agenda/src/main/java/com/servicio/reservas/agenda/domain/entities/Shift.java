package com.servicio.reservas.agenda.domain.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class Shift {
    private Long id;
    private Long barber_id;
    private LocalDate date;
    private LocalTime time_start;
    private LocalTime time_end;
    private String state;
}
