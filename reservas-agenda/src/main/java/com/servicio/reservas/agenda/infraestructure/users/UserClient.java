package com.servicio.reservas.agenda.infraestructure.users;

import com.servicio.reservas.agenda.infraestructure.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
        name = "reservas-usuarios-service",
        configuration = FeignConfig.class)
public interface UserClient {
    @GetMapping("/api/users/user/{id}")
    Optional<UserDTO> findUserById(@PathVariable("id") Long id);

}