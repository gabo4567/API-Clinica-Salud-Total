package com.saludtotal.dto;

import java.io.Serializable;

public class TurnosPorEstadoDTO implements Serializable {

    private String especialidad;
    private Long totalTurnos;
    private Long cancelados;
    private Long reprogramados;
    private Long atendidos;

    public TurnosPorEstadoDTO(String especialidad, Long totalTurnos, Long cancelados, Long reprogramados, Long atendidos) {
        this.especialidad = especialidad;
        this.totalTurnos = totalTurnos;
        this.cancelados = cancelados;
        this.reprogramados = reprogramados;
        this.atendidos = atendidos;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Long getTotalTurnos() {
        return totalTurnos;
    }

    public void setTotalTurnos(Long totalTurnos) {
        this.totalTurnos = totalTurnos;
    }

    public Long getCancelados() {
        return cancelados;
    }

    public void setCancelados(Long cancelados) {
        this.cancelados = cancelados;
    }

    public Long getReprogramados() {
        return reprogramados;
    }

    public void setReprogramados(Long reprogramados) {
        this.reprogramados = reprogramados;
    }

    public Long getAtendidos() {
        return atendidos;
    }

    public void setAtendidos(Long atendidos) {
        this.atendidos = atendidos;
    }
}