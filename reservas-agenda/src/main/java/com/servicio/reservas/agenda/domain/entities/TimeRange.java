package com.servicio.reservas.agenda.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class TimeRange {
    private LocalTime start;
    private LocalTime end;

    public boolean isContained(LocalTime hourStart, LocalTime hourEnd){
        return (hourStart.equals(start) || hourStart.isAfter(start))
                && (hourEnd.equals(end) || hourEnd.isBefore(end));
    }
}
