package com.servicio.reservas.agenda.infraestructure.output.client.services;

import com.servicio.reservas.agenda.infraestructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
        name = "reservas-servicios-service",
        configuration = FeignConfig.class)

public interface ServiceClient {
    @GetMapping("/api/services/{id}")
    Optional<ServiceDTO> findServiceById(@PathVariable("id") Long id);
}