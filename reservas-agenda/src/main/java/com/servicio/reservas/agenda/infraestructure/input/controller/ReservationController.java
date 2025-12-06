package com.servicio.reservas.agenda.infraestructure.input.controller;

import com.servicio.reservas.agenda.application.dto.reservations.filters.FilterReservationAdmin;
import com.servicio.reservas.agenda.application.dto.reservations.filters.FilterReservationUser;
import com.servicio.reservas.agenda.application.dto.reservations.RequestReservation;
import com.servicio.reservas.agenda.application.dto.reservations.ResponseReservation;
import com.servicio.reservas.agenda.application.services.reservations.ReservationService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        return ResponseEntity.ok("Successful Cancellation");
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<String>  deactivateReservation(@PathVariable Long id) {
        reservationService.deactivateReservation(id);
        return ResponseEntity.ok("Successful Deactivation");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseReservation>  findReservationById(@PathVariable Long id) {
        ResponseReservation response = reservationService.findReservationById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search-user")
    public ResponseEntity<List<ResponseReservation>>  getUserReservations(

            @RequestParam Long userId,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @RequestParam(required = false) String status
    ) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de inicio debe ser anterior o igual a la fecha de fin.");
        }

        FilterReservationUser filters = new FilterReservationUser();
        filters.setUserId(userId);
        filters.setStartDate(startDate);
        filters.setEndDate(endDate);
        filters.setStatus(status != null ? status.toUpperCase() : null);

        List<ResponseReservation> result = reservationService.searchReservationsUser(filters);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/search-admin")
    public ResponseEntity<List<ResponseReservation>> getAllReservationsAdmin(

            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long serviceId,
            @RequestParam(required = false) String status,

            @RequestParam(required = false)
            LocalDate startDate,

            @RequestParam(required = false)
            LocalDate endDate
    ) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de inicio debe ser anterior o igual a la fecha de fin.");
        }
        // Crear DTO de filtros
        FilterReservationAdmin filters = new FilterReservationAdmin();
        filters.setUserId(userId);
        filters.setServiceId(serviceId);
        filters.setStartDate(startDate);
        filters.setEndDate(endDate);
        filters.setStatus(status != null ? status.toUpperCase() : null);

        // Llamar al service
        List<ResponseReservation> result = reservationService.searchAllReservationsAdmin(filters);
        return ResponseEntity.ok(result);
    }

}

