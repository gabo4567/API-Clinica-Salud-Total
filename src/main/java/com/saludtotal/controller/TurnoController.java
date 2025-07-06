package com.saludtotal.controller;

import com.saludtotal.dto.*;
import com.saludtotal.exceptions.TurnoSuperpuestoException;
import com.saludtotal.service.TurnoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @GetMapping("/reportes/turnos-por-dia")
    public ResponseEntity<List<CantidadTurnosPorDiaDTO>> getTurnosPorDia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        List<CantidadTurnosPorDiaDTO> resultado = turnoService.obtenerCantidadTurnosPorDia(fechaInicio, fechaFin);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/reportes/turnos-por-profesional")
    public ResponseEntity<List<CantidadTurnosPorProfesionalDTO>> getCantidadTurnosPorProfesional(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) String nombreProfesional) {

        List<CantidadTurnosPorProfesionalDTO> resultado = turnoService.obtenerCantidadTurnosPorProfesionalEnRango(
                fechaInicio, fechaFin, nombreProfesional
        );
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/reportes/pacientes-por-especialidad")
    public ResponseEntity<List<PacientesAtendidosPorEspecialidadDTO>> getPacientesPorEspecialidad(
            @RequestParam(required = false) String especialidad,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        List<PacientesAtendidosPorEspecialidadDTO> resultado = turnoService.obtenerPacientesAtendidosPorEspecialidad(especialidad, fechaInicio, fechaFin);
        return ResponseEntity.ok(resultado);
    }



    // Crear nuevo turno
    @PostMapping
    public ResponseEntity<?> crearTurno(@RequestBody RegistroTurnoDTO registroTurnoDTO) {
        try {
            // ✅ Prints de depuración
            System.out.println(">> ID Profesional recibido: " + registroTurnoDTO.getIdProfesional());
            System.out.println(">> ID Paciente recibido: " + registroTurnoDTO.getIdPaciente());
            System.out.println(">> FechaHora recibida: " + registroTurnoDTO.getFechaHora());
            System.out.println(">> idEstado recibido: " + registroTurnoDTO.getIdEstado());

            // Convierte string a LocalDateTime
            LocalDateTime fechaHora = LocalDateTime.parse(registroTurnoDTO.getFechaHora());

            // Generar comprobante nuevo con formato ST-YYYYMMDD-000001
            String comprobante = turnoService.generarNuevoComprobante(fechaHora.toLocalDate());

            // Si idEstado es null, asignar por defecto 10L (programado)
            Long idEstado = registroTurnoDTO.getIdEstado();
            if (idEstado == null) {
                idEstado = 10L;
                System.out.println(">> idEstado estaba null, se asignó 10L por defecto.");
            }

            // Construye TurnoDTO para pasar al servicio
            TurnoDTO turnoDTO = new TurnoDTO();
            turnoDTO.setComprobante(comprobante);
            turnoDTO.setIdPaciente(registroTurnoDTO.getIdPaciente());
            turnoDTO.setIdProfesional(registroTurnoDTO.getIdProfesional());
            turnoDTO.setFechaHora(fechaHora);
            turnoDTO.setDuracion(registroTurnoDTO.getDuracion());
            turnoDTO.setIdEstado(idEstado);
            turnoDTO.setObservaciones(registroTurnoDTO.getObservaciones());

            System.out.println(">> TurnoDTO creado para servicio: " + turnoDTO);

            TurnoDTO creado = turnoService.crearTurno(turnoDTO);

            System.out.println(">> Turno creado con ID: " + creado.getId());

            return ResponseEntity.ok(creado);

        } catch (TurnoSuperpuestoException e) {
            // ⚠️ Error de superposición de turnos
            System.out.println(">> ERROR: Superposición de turno detectada: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)  // HTTP 409
                    .body("Ya existe un turno en ese horario para este profesional.");
        } catch (Exception e) {
            // Otro error inesperado
            System.out.println(">> Error general al crear turno: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error al crear el turno.");
        }
    }

    // Actualizar turno
    @PutMapping("/{id}")
    public ResponseEntity<TurnoDTO> actualizarTurno(@PathVariable Long id, @RequestBody TurnoDTO turnoDTO) {
        System.out.println("DEBUG - ID recibido en backend para actualizar turno: " + id);
        if (id == null) {
            System.out.println("ERROR - El ID recibido es null");
            return ResponseEntity.badRequest().build();
        }

        System.out.println("DEBUG - DTO recibido para actualizar turno: " + turnoDTO);

        TurnoDTO actualizado = turnoService.actualizarTurno(id, turnoDTO);

        System.out.println("DEBUG - Turno actualizado devuelto por servicio: " + actualizado);

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
