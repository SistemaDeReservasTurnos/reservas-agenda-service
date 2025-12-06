package com.servicio.reservas.agenda.infraestructure.output.client.users;

import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String phoneNumber;
    private String role;
}