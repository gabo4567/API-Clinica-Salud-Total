package com.saludtotal.dto;

import java.util.List;

public class ResultadoTurnosPorDiaDTO {
    private List<CantidadTurnosPorDiaDTO> turnosPorDia;
    private Long totalTurnos;

    public ResultadoTurnosPorDiaDTO(List<CantidadTurnosPorDiaDTO> turnosPorDia, Long totalTurnos) {
        this.turnosPorDia = turnosPorDia;
        this.totalTurnos = totalTurnos;
    }

    public List<CantidadTurnosPorDiaDTO> getTurnosPorDia() {
        return turnosPorDia;
    }

    public void setTurnosPorDia(List<CantidadTurnosPorDiaDTO> turnosPorDia) {
        this.turnosPorDia = turnosPorDia;
    }

    public Long getTotalTurnos() {
        return totalTurnos;
    }

    public void setTotalTurnos(Long totalTurnos) {
        this.totalTurnos = totalTurnos;
    }
}