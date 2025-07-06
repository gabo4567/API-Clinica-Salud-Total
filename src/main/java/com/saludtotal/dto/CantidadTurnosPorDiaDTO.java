package com.saludtotal.dto;

import java.time.LocalDate;

public class CantidadTurnosPorDiaDTO {
    private LocalDate fecha;
    private long cantidad;

    public CantidadTurnosPorDiaDTO(LocalDate fecha, long cantidad) {
        this.fecha = fecha;
        this.cantidad = cantidad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public long getCantidad() {
        return cantidad;
    }
}
