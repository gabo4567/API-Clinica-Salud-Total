package com.saludtotal.dto;

public class ReporteTurnosAtendidosDTO {
    private Long idProfesional;
    private String nombreProfesional;
    private long cantidadTurnosAtendidos;

    // Constructor vacío
    public ReporteTurnosAtendidosDTO() {
    }

    // Constructor
    public ReporteTurnosAtendidosDTO(Long idProfesional, String nombreProfesional, long cantidadTurnosAtendidos) {
        this.idProfesional = idProfesional;
        this.nombreProfesional = nombreProfesional;
        this.cantidadTurnosAtendidos = cantidadTurnosAtendidos;
    }

    public Long getIdProfesional() {
        return idProfesional;
    }

    public String getNombreProfesional() {
        return nombreProfesional;
    }

    public long getCantidadTurnosAtendidos() {
        return cantidadTurnosAtendidos;
    }
}
