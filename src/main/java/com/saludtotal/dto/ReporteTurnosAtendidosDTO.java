package com.saludtotal.dto;

public class ReporteTurnosAtendidosDTO {
    private Integer idProfesional;
    private String nombreProfesional;
    private long cantidadTurnosAtendidos;

    public ReporteTurnosAtendidosDTO(Integer idProfesional, String nombreProfesional, long cantidadTurnosAtendidos) {
        this.idProfesional = idProfesional;
        this.nombreProfesional = nombreProfesional;
        this.cantidadTurnosAtendidos = cantidadTurnosAtendidos;
    }

    public Integer getIdProfesional() {
        return idProfesional;
    }

    public String getNombreProfesional() {
        return nombreProfesional;
    }

    public long getCantidadTurnosAtendidos() {
        return cantidadTurnosAtendidos;
    }
}
