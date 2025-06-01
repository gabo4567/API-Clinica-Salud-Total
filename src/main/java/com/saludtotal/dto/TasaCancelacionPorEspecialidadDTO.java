package com.saludtotal.dto;

public class TasaCancelacionPorEspecialidadDTO {
    private String especialidad;
    private long totalTurnos;
    private long turnosCancelados;
    private double tasaCancelacion;

    // Constructor vacío
    public TasaCancelacionPorEspecialidadDTO() {
    }

    // Constructor
    public TasaCancelacionPorEspecialidadDTO(String especialidad, long totalTurnos, long turnosCancelados, double tasaCancelacion) {
        this.especialidad = especialidad;
        this.totalTurnos = totalTurnos;
        this.turnosCancelados = turnosCancelados;
        this.tasaCancelacion = tasaCancelacion;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public long getTotalTurnos() {
        return totalTurnos;
    }

    public void setTotalTurnos(long totalTurnos) {
        this.totalTurnos = totalTurnos;
    }

    public long getTurnosCancelados() {
        return turnosCancelados;
    }

    public void setTurnosCancelados(long turnosCancelados) {
        this.turnosCancelados = turnosCancelados;
    }

    public double getTasaCancelacion() {
        return tasaCancelacion;
    }

    public void setTasaCancelacion(double tasaCancelacion) {
        this.tasaCancelacion = tasaCancelacion;
    }
}
