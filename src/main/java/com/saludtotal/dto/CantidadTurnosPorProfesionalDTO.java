package com.saludtotal.dto;

public class CantidadTurnosPorProfesionalDTO {

    private Long idProfesional;
    private String nombreProfesional;
    private Long cantidadTurnos;

    public CantidadTurnosPorProfesionalDTO(Long idProfesional, String nombreProfesional, Long cantidadTurnos) {
        this.idProfesional = idProfesional;
        this.nombreProfesional = nombreProfesional;
        this.cantidadTurnos = cantidadTurnos;
    }

    // Getters y setters

    public Long getIdProfesional() {
        return idProfesional;
    }

    public void setIdProfesional(Long idProfesional) {
        this.idProfesional = idProfesional;
    }

    public String getNombreProfesional() {
        return nombreProfesional;
    }

    public void setNombreProfesional(String nombreProfesional) {
        this.nombreProfesional = nombreProfesional;
    }

    public Long getCantidadTurnos() {
        return cantidadTurnos;
    }

    public void setCantidadTurnos(Long cantidadTurnos) {
        this.cantidadTurnos = cantidadTurnos;
    }
}
