package com.saludtotal.controller;

import com.saludtotal.clinica.models.Turno;
import com.saludtotal.service.TurnoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/turnos")
public class TurnoController {

    private final TurnoService turnoService;

    public TurnoController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    // Obtener turnos futuros de un paciente
    @GetMapping("/paciente/{id}/futuros")
    public ResponseEntity<List<Turno>> getTurnosFuturosPorPaciente(@PathVariable Integer id) {
        List<Turno> turnosFuturos = turnoService.obtenerTurnosFuturosPorPaciente(id);
        return ResponseEntity.ok(turnosFuturos);
    }

    // Obtener turnos pasados de un paciente
    @GetMapping("/paciente/{id}/pasados")
    public ResponseEntity<List<Turno>> getTurnosPasadosPorPaciente(@PathVariable Integer id) {
        List<Turno> turnosPasados = turnoService.obtenerTurnosPasadosPorPaciente(id);
        return ResponseEntity.ok(turnosPasados);
    }
}