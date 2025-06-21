package com.saludtotal.dto;

import java.time.LocalDate;
import java.util.List;

public class HorarioDisponibleDTO {
    private LocalDate fecha;
    private List<String> horarios;

    public HorarioDisponibleDTO() {
    }

    public HorarioDisponibleDTO(LocalDate fecha, List<String> horarios) {
        this.fecha = fecha;
        this.horarios = horarios;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public List<String> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<String> horarios) {
        this.horarios = horarios;
    }
}