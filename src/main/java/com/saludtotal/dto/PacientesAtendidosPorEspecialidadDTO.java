package com.saludtotal.dto;

import java.io.Serializable;

public class PacientesAtendidosPorEspecialidadDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String especialidad;
    private long cantidadPacientes;

    public PacientesAtendidosPorEspecialidadDTO() {
        // Constructor vacío para frameworks como Jackson
    }

    public PacientesAtendidosPorEspecialidadDTO(String especialidad, long cantidadPacientes) {
        this.especialidad = especialidad;
        this.cantidadPacientes = cantidadPacientes;
    }

    // Getters y Setters
    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public long getCantidadPacientes() {
        return cantidadPacientes;
    }

    public void setCantidadPacientes(long cantidadPacientes) {
        this.cantidadPacientes = cantidadPacientes;
    }

    @Override
    public String toString() {
        return "PacientesAtendidosPorEspecialidadDTO{" +
                "especialidad='" + especialidad + '\'' +
                ", cantidadPacientes=" + cantidadPacientes +
                '}';
    }
}