package com.saludtotal.controller;

import com.saludtotal.dto.ReporteTurnosAtendidosDTO;
import com.saludtotal.dto.ReporteTurnosCanceladosYReprogramadosDTO;
import com.saludtotal.dto.TasaCancelacionPorEspecialidadDTO;
import com.saludtotal.dto.TurnoDTO;
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

    // Obtener todos los turnos
    @GetMapping
    public ResponseEntity<List<TurnoDTO>> listarTodos() {
        return ResponseEntity.ok(turnoService.listarTodos());
    }

    // Obtener turnos futuros de un paciente
    @GetMapping("/paciente/{id}/futuros")
    public ResponseEntity<List<TurnoDTO>> getTurnosFuturosPorPaciente(@PathVariable Long id) {
        return ResponseEntity.ok(turnoService.obtenerTurnosFuturosPorPaciente(id));
    }

    // Obtener turnos pasados de un paciente
    @GetMapping("/paciente/{id}/pasados")
    public ResponseEntity<List<TurnoDTO>> getTurnosPasadosPorPaciente(@PathVariable Long id) {
        return ResponseEntity.ok(turnoService.obtenerTurnosPasadosPorPaciente(id));
    }

    // Obtener turno por ID
    @GetMapping("/{id}")
    public ResponseEntity<TurnoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(turnoService.obtenerDTOporId(id));
    }

    // Crear nuevo turno
    @PostMapping
    public ResponseEntity<TurnoDTO> crearTurno(@RequestBody TurnoDTO turnoDTO) {
        TurnoDTO creado = turnoService.crearTurno(turnoDTO);
        return ResponseEntity.ok(creado);
    }

    // Actualizar turno
    @PutMapping("/{id}")
    public ResponseEntity<TurnoDTO> actualizarTurno(@PathVariable Long id, @RequestBody TurnoDTO turnoDTO) {
        TurnoDTO actualizado = turnoService.actualizarTurno(id, turnoDTO);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar turno
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTurno(@PathVariable Long id) {
        turnoService.eliminarTurno(id);
        return ResponseEntity.noContent().build();
    }


    // Buscar turnos por profesional
    @GetMapping("/profesional/{id}")
    public ResponseEntity<List<TurnoDTO>> getTurnosPorProfesional(@PathVariable Long id) {
        List<TurnoDTO> turnosDTO = turnoService.obtenerTurnosPorProfesional(id);
        return ResponseEntity.ok(turnosDTO);
    }

    // Buscar turnos por estado
    @GetMapping("/estado/{id}")
    public ResponseEntity<List<TurnoDTO>> getTurnosPorEstado(@PathVariable Long id) {
        List<TurnoDTO> turnosDTO = turnoService.obtenerTurnosPorEstado(id);
        return ResponseEntity.ok(turnosDTO);
    }


    // Buscar turnos por fecha (un día)
    @GetMapping("/fecha")
    public ResponseEntity<List<TurnoDTO>> getTurnosPorFecha(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(turnoService.obtenerTurnosPorFecha(fecha));
    }


    // Buscar turnos por profesional en un día (disponibilidad)
    @GetMapping("/profesional/{id}/disponibilidad")
    public ResponseEntity<List<TurnoDTO>> getTurnosPorProfesionalEnFecha(
            @PathVariable Long id,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(turnoService.obtenerTurnosDeProfesionalEnFecha(id, fecha));
    }


    // Turnos atendidos por profesional en un rango de fechas
    @GetMapping("/reportes/turnos-atendidos")
    public ResponseEntity<ReporteTurnosAtendidosDTO> getCantidadTurnosAtendidosPorProfesional(
            @RequestParam Long profesionalId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        ReporteTurnosAtendidosDTO resultado = turnoService.obtenerCantidadTurnosAtendidosPorProfesionalEnRango(profesionalId, fechaInicio, fechaFin);
        return ResponseEntity.ok(resultado);
    }

    // Turnos cancelados y reprogramados
    @GetMapping("/reportes/turnos-cancelados-reprogramados")
    public ResponseEntity<ReporteTurnosCanceladosYReprogramadosDTO> getReporteCanceladosYReprogramados(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        ReporteTurnosCanceladosYReprogramadosDTO resultado =
                turnoService.obtenerReporteCanceladosYReprogramados(fechaInicio, fechaFin);
        return ResponseEntity.ok(resultado);
    }

    // Tasa de cancelación según especialidad
    @GetMapping("/reportes/tasa-cancelacion-por-especialidad")
    public ResponseEntity<List<TasaCancelacionPorEspecialidadDTO>> obtenerTasaCancelacionPorEspecialidad(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        List<TasaCancelacionPorEspecialidadDTO> resultado = turnoService.obtenerTasaCancelacionPorEspecialidad(fechaInicio, fechaFin);
        return ResponseEntity.ok(resultado);
    }

}
