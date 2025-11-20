package com.servicio.reservas.agenda.infraestructure.persistence.reservations;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity(name = "reservation")
@Table(name = "reservation")
@Data
@NoArgsConstructor

public class ReservationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long serviceId;

    @Column
    private Long userId;

    @Column
    private Long barberId;

    @Column
    private LocalDate date;

    @Column
    private LocalTime startTime;

    @Column
    private LocalTime endTime;

    @Column
    private String status;

    @Column
    private Boolean active;

}
