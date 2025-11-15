package com.servicio.reservas.agenda.infraestructure.controller;

import com.servicio.reservas.agenda.application.dto.RequestReservation;
import com.servicio.reservas.agenda.application.dto.ResponseReservation;
import com.servicio.reservas.agenda.application.services.ReservationService;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseReservation> create(@Valid @RequestBody RequestReservation request) {

        ResponseReservation response = reservationService.createReservation(request);
        return ResponseEntity.ok(response);
    }
}
