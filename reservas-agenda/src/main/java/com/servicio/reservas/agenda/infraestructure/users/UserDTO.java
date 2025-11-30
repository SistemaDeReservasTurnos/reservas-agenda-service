package com.servicio.reservas.agenda.infraestructure.users;

import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String phone_number;
    private String role;
}
