package com.servicio.reservas.agenda.application.usecase;

import com.servicio.reservas.agenda.domain.entities.Reservation;
import com.servicio.reservas.agenda.domain.repository.IReservationRepository;
import com.servicio.reservas.agenda.domain.repository.IShiftRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class FinalizeReservationsUseCase {

    private final IReservationRepository reservationRepository;
    private final IShiftRepository shiftRepository;

    public FinalizeReservationsUseCase(IReservationRepository reservationRepository,  IShiftRepository shiftRepository) {
        this.reservationRepository = reservationRepository;
        this.shiftRepository = shiftRepository;
    }

    public void execute() {
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();

        List<Reservation> toFinalize =
                reservationRepository.findAllActiveThatEnded(now, today);

        for (Reservation r : toFinalize) {
            r.setStatus("COMPLETED");
            r.setActive(false);
            reservationRepository.save(r);

            shiftRepository.updateStateShiftByReservationId(r.getId());
        }
    }
}
