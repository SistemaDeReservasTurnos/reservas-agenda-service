package com.servicio.reservas.agenda.infraestructure.input.scheduler;

import com.servicio.reservas.agenda.application.usecase.FinalizeReservationsUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReservationScheduler {

    private final FinalizeReservationsUseCase finalizeReservationsUseCase;

    public ReservationScheduler(FinalizeReservationsUseCase finalizeReservationsUseCase) {
        this.finalizeReservationsUseCase = finalizeReservationsUseCase;
    }

    @Scheduled(cron = "0 * * * * *")
    public void run() {
        finalizeReservationsUseCase.execute();
    }
}
