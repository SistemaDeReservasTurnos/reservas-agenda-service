package com.servicio.reservas.agenda.infraestructure.controller;

import com.servicio.reservas.agenda.application.dto.FilterReservationUser;
import com.servicio.reservas.agenda.application.dto.RequestReservation;
import com.servicio.reservas.agenda.application.dto.ResponseReservation;
import com.servicio.reservas.agenda.application.services.ReservationService;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseReservation>  editReservation(@PathVariable Long id, @Valid @RequestBody RequestReservation request) {
        ResponseReservation response = reservationService.editReservation(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<String>  cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.ok("cancelación exitosa");
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<String>  deactivateReservation(@PathVariable Long id) {
        reservationService.deactivateReservation(id);
        return ResponseEntity.ok("desactivación exitosa");
    }
    //@DeleteMapping("/delete/{id}")

    @GetMapping("/{id}")
    public ResponseEntity<ResponseReservation>  findReservationById(@PathVariable Long id) {
        ResponseReservation response = reservationService.findReservationById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/user/{id}")
    public List<ResponseReservation> getUserReservations(
            @PathVariable("id") Long userId,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,

            @RequestParam(required = false) String status
    ) {

        FilterReservationUser filters = new FilterReservationUser();
        filters.setUserId(userId);
        filters.setStartDate(startDate);
        filters.setEndDate(endDate);
        filters.setStatus(status);

        return reservationService.searchReservations(filters);
    }

}

