package com.servicio.reservas.agenda.infraestructure.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.servicio.reservas.agenda.application.services.TimeBlocksService;


import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/time-blocks")
@RequiredArgsConstructor
public class TimeBlocksController {
    private final TimeBlocksService timeBlocksService;

    @GetMapping("/availability")
    public ResponseEntity<Boolean> verifyAvailability(
            @RequestParam Long idBarber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        boolean available = timeBlocksService
                .validate(idBarber, date, startTime, endTime);
        return ResponseEntity.ok(available);
    }
}
