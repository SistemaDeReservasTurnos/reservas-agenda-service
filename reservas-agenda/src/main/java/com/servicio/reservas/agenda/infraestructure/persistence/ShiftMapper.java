package com.servicio.reservas.agenda.infraestructure.persistence;

import com.servicio.reservas.agenda.domain.entities.Shift;

public class ShiftMapper {

    public static ShiftModel toModel(Shift shift) {
        ShiftModel model = new ShiftModel();
        model.setId(shift.getId());
        model.setBarberId(shift.getBarberId());
        model.setDate(shift.getDate());
        model.setTimeStart(shift.getTimeStart());
        model.setTimeEnd(shift.getTimeEnd());
        model.setReservationId(shift.getReservationId());
        model.setState("RESERVED");
        return model;
    }

    public static Shift toDomain(ShiftModel model) {
        return new Shift(
                model.getId(),
                model.getBarberId(),
                model.getDate(),
                model.getTimeStart(),
                model.getTimeEnd(),
                model.getReservationId(),
                model.getState()
        );
    }
}
