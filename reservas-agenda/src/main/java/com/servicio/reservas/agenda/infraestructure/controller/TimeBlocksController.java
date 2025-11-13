package com.servicio.reservas.agenda.infraestructure.controller;

import com.servicio.reservas.agenda.application.dto.RequestShift;
import com.servicio.reservas.agenda.application.services.ShiftService;
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

        boolean available = shiftService
                .validateAvailabilityBarber(idBarber, date, startTime, endTime);
        return ResponseEntity.ok(available);
    }

    @PostMapping("/create")
    public ResponseEntity<Shift> create(@RequestBody RequestShift request) {
        Shift shiftResponse = shiftService.createShift
                (request.getBarberId(), request.getDate(), request.getTimeStart(), request.getTimeEnd());
        return ResponseEntity.ok(shiftResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        shiftService.deleteShift(id);
        return ResponseEntity.ok("Shift deleted successfully");
    }
}
