package com.servicio.reservas.agenda.infraestructure.services;

import lombok.Data;

@Data
public class ServiceDTO {
    private String name;
    private String description;
    private String duration;
    private Double price;
}
