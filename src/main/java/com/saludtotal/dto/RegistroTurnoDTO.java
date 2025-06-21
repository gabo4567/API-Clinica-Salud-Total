package com.saludtotal.dto;

import jakarta.validation.constraints.NotNull;

public class RegistroTurnoDTO {

    private String comprobante;

    @NotNull(message = "El id del paciente es obligatorio")
    private Long idPaciente;

    @NotNull(message = "El id del profesional es obligatorio")
    private Long idProfesional;

    @NotNull(message = "La fecha y hora es obligatoria")
    private String fechaHora;  // Fecha y hora en formato ISO 8601 como String

    @NotNull(message = "La duración es obligatoria")
    private Integer duracion;

    @NotNull(message = "El id del estado es obligatorio")
    private Long idEstado;

    private String observaciones;

    // Constructor vacío
    public RegistroTurnoDTO() {
    }

    // Getters y Setters

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public Long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Long getIdProfesional() {
        return idProfesional;
    }

    public void setIdProfesional(Long idProfesional) {
        this.idProfesional = idProfesional;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public Long getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Long idEstado) {
        this.idEstado = idEstado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
