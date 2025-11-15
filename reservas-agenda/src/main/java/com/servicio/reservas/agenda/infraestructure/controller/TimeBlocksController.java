package com.servicio.reservas.agenda.infraestructure.controller;

import com.servicio.reservas.agenda.application.AvailabilityMode;
import com.servicio.reservas.agenda.application.dto.RequestShift;
import com.servicio.reservas.agenda.application.services.ShiftService;
import com.servicio.reservas.agenda.domain.entities.Reservation;
import com.servicio.reservas.agenda.domain.entities.Shift;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/time-blocks")
@RequiredArgsConstructor
public class TimeBlocksController {
    private final ShiftService  shiftService;

    @GetMapping("/availability")
    public ResponseEntity<Boolean> verifyAvailability(
            @RequestParam Long idBarber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        Reservation reservation = new Reservation();
        reservation.setId(idBarber);
        reservation.setDate(date);
        reservation.setTimeStart(startTime);
        reservation.setTimeEnd(endTime);

        boolean available = shiftService
                .validateAvailabilityBarber(reservation, AvailabilityMode.CREATE);
        return ResponseEntity.ok(available);
    }

    @PostMapping("/create")
    public ResponseEntity<Shift> create(@RequestBody RequestShift request) {

        Reservation reservation = new Reservation();
        reservation.setBarberId(request.getBarberId());
        reservation.setDate(request.getDate());
        reservation.setTimeStart(request.getTimeStart());
        reservation.setTimeEnd(request.getTimeEnd());

        Shift shift = shiftService.createShift(reservation);
        return ResponseEntity.ok(shift);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        shiftService.deleteShift(id);
        return ResponseEntity.ok("Shift deleted successfully");
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Shift> updateShift(
            @PathVariable Long id,
            @RequestBody RequestShift request) {

        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setBarberId(request.getBarberId());
        reservation.setDate(request.getDate());
        reservation.setTimeStart(request.getTimeStart());
        reservation.setTimeEnd(request.getTimeEnd());

        Shift updatedShift = shiftService.updateShift(reservation);
        return ResponseEntity.ok(updatedShift);
    }
}
