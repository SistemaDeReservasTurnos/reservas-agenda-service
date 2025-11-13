package com.servicio.reservas.agenda.infraestructure.services;

import lombok.Data;
import java.time.LocalTime;

@Data
public class ServiceDTO {
    private String name;
    private String description;
    private LocalTime duration;
    private Double price;
}
