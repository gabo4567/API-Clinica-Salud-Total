package com.saludtotal.dto;

public class ReporteTurnosCanceladosYReprogramadosDTO {
    private long cantidadCancelados;
    private long cantidadReprogramados;

    // Constructor vacío
    public ReporteTurnosCanceladosYReprogramadosDTO(){
    }

    // Constructor
    public ReporteTurnosCanceladosYReprogramadosDTO(long cantidadCancelados, long cantidadReprogramados) {
        this.cantidadCancelados = cantidadCancelados;
        this.cantidadReprogramados = cantidadReprogramados;
    }

    public long getCantidadCancelados() {
        return cantidadCancelados;
    }

    public long getCantidadReprogramados() {
        return cantidadReprogramados;
    }
}
