package com.saludtotal.controller;

import com.saludtotal.clinica.models.Turno;
import com.saludtotal.dto.ReporteTurnosAtendidosDTO;
import com.saludtotal.service.TurnoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
        return ResponseEntity.ok(turnoService.obtenerTurnosFuturosPorPaciente(id));
    }

    // Obtener turnos pasados de un paciente
    @GetMapping("/paciente/{id}/pasados")
    public ResponseEntity<List<Turno>> getTurnosPasadosPorPaciente(@PathVariable Integer id) {
        return ResponseEntity.ok(turnoService.obtenerTurnosPasadosPorPaciente(id));
    }

    // Obtener turno por ID
    @GetMapping("/{id}")
    public ResponseEntity<Turno> getTurnoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(turnoService.obtenerPorId(id));
    }

    // Crear nuevo turno
    @PostMapping
    public ResponseEntity<Turno> crearTurno(@RequestBody Turno turno) {
        return ResponseEntity.ok(turnoService.crearTurno(turno));
    }

    // Actualizar turno
    @PutMapping("/{id}")
    public ResponseEntity<Turno> actualizarTurno(@PathVariable Long id, @RequestBody Turno turnoActualizado) {
        return ResponseEntity.ok(turnoService.actualizarTurno(id, turnoActualizado));
    }

    // Eliminar turno
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTurno(@PathVariable Long id) {
        turnoService.eliminarTurno(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar turnos por profesional
    @GetMapping("/profesional/{id}")
    public ResponseEntity<List<Turno>> getTurnosPorProfesional(@PathVariable Integer id) {
        return ResponseEntity.ok(turnoService.obtenerTurnosPorProfesional(id));
    }

    // Buscar turnos por estado
    @GetMapping("/estado/{id}")
    public ResponseEntity<List<Turno>> getTurnosPorEstado(@PathVariable Integer id) {
        return ResponseEntity.ok(turnoService.obtenerTurnosPorEstado(id));
    }

    // Buscar turnos por fecha (un día)
    @GetMapping("/fecha")
    public ResponseEntity<List<Turno>> getTurnosPorFecha(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(turnoService.obtenerTurnosPorFecha(fecha));
    }

    // Buscar turnos por profesional en un día (disponibilidad)
    @GetMapping("/profesional/{id}/disponibilidad")
    public ResponseEntity<List<Turno>> getTurnosPorProfesionalEnFecha(
            @PathVariable Integer id,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(turnoService.obtenerTurnosDeProfesionalEnFecha(id, fecha));
    }

    // Turnos atendidos por profesional en un rango de fechas
    @GetMapping("/reportes/turnos-atendidos")
    public ResponseEntity<ReporteTurnosAtendidosDTO> getCantidadTurnosAtendidosPorProfesional(
            @RequestParam Integer profesionalId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        ReporteTurnosAtendidosDTO resultado = turnoService.obtenerCantidadTurnosAtendidosPorProfesionalEnRango(profesionalId, fechaInicio, fechaFin);
        return ResponseEntity.ok(resultado);
    }


}
