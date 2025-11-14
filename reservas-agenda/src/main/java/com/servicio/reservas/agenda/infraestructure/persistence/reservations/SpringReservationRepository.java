package com.servicio.reservas.agenda.infraestructure.persistence.reservations;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringReservationRepository extends JpaRepository<ReservationModel, Long> {
}
