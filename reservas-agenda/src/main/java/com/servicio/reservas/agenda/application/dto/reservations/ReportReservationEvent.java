package com.servicio.reservas.agenda.application.dto.reservations;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReportReservationEvent {
    private Long id;
    private Long serviceId;
    private String nameService;
    private Long userId;
    private String nameUser;
    private Long barberId;
    private String nameBarber;
    private LocalDate date;
    private String status;
    private Double amount;
}
