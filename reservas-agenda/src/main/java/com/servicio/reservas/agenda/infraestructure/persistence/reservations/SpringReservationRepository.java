package com.servicio.reservas.agenda.infraestructure.persistence.reservations;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringReservationRepository extends JpaRepository<ReservationModel, Long> {

    @NotNull Optional<ReservationModel> findById(@NotNull Long id);
}
