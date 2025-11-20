package com.servicio.reservas.agenda.infraestructure.controller;

import com.servicio.reservas.agenda.application.dto.RequestReservation;
import com.servicio.reservas.agenda.application.dto.ResponseReservation;
import com.servicio.reservas.agenda.application.services.ReservationService;
import com.servicio.reservas.agenda.domain.entities.Reservation;
import com.servicio.reservas.agenda.domain.repository.IReservationRepository;
import com.servicio.reservas.agenda.infraestructure.persistence.reservations.ReservationModel;
import com.servicio.reservas.agenda.infraestructure.persistence.reservations.Specifications.SearchReservationUserSpecification;
import com.servicio.reservas.agenda.infraestructure.persistence.reservations.SpringReservationRepository;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final SpringReservationRepository springReservationRepository;

    public ReservationController(ReservationService reservationService, SpringReservationRepository  springReservationRepository) {
        this.reservationService = reservationService;
        this.springReservationRepository = springReservationRepository;
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

    @GetMapping("/search")
    List<ReservationModel> getUserReservations(@RequestParam(required = false) Long userId, @RequestParam(required = false) LocalDate from, @RequestParam(required = false) LocalDate to, @RequestParam(required = false) String status) {

        SearchReservationUserSpecification searchReservationUserSpecification = new SearchReservationUserSpecification(userId, from, to, status);

        return springReservationRepository.findAll(searchReservationUserSpecification);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseReservation>  findReservationById(@PathVariable Long id) {
        ResponseReservation response = reservationService.findReservationById(id);
        return ResponseEntity.ok(response);
    }
}

