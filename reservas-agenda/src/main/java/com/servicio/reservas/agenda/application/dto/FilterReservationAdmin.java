package com.servicio.reservas.agenda.application.dto;

import java.time.LocalDate;

public class FilterReservationAdmin {

    private Long userId;          // opcional
    private Long serviceId;       // opcional
    private String status;        // opcional
    private LocalDate startDate;  // opcional
    private LocalDate endDate;    // opcional

    // ---- Getters & Setters ----

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}



