package com.saludtotal.service;

import com.saludtotal.clinica.models.Estado;
import com.saludtotal.clinica.models.Persona;
import com.saludtotal.clinica.models.Turno;
import com.saludtotal.dto.ReporteTurnosAtendidosDTO;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.repositories.TurnoRepository;
import com.saludtotal.repositories.PacienteRepository;
import com.saludtotal.repositories.PersonaRepository;
import com.saludtotal.repositories.EstadoRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class TurnoService {

    private final TurnoRepository turnoRepository;
    private final PacienteRepository pacienteRepository;
    private final PersonaRepository personaRepository;
    private final EstadoRepository estadoRepository;

    public TurnoService(TurnoRepository turnoRepository, PacienteRepository pacienteRepository,
                        PersonaRepository personaRepository, EstadoRepository estadoRepository) {
        this.turnoRepository = turnoRepository;
        this.pacienteRepository = pacienteRepository;
        this.personaRepository = personaRepository;
        this.estadoRepository = estadoRepository;
    }

    // Obtener turnos futuros por paciente
    public List<Turno> obtenerTurnosFuturosPorPaciente(Integer pacienteId) {
        validarPaciente(pacienteId);
        List<Turno> turnos = turnoRepository.findByPacienteIdAndFechaHoraAfter(pacienteId, LocalDateTime.now());
        if (turnos.isEmpty()) {
            throw new RecursoNoEncontradoException("No se encontraron turnos futuros para el paciente con ID: " + pacienteId);
        }
        return turnos;
    }

    // Obtener turnos pasados por paciente
    public List<Turno> obtenerTurnosPasadosPorPaciente(Integer pacienteId) {
        validarPaciente(pacienteId);
        List<Turno> turnos = turnoRepository.findByPacienteIdAndFechaHoraBefore(pacienteId, LocalDateTime.now());
        if (turnos.isEmpty()) {
            throw new RecursoNoEncontradoException("No se encontraron turnos pasados para el paciente con ID: " + pacienteId);
        }
        return turnos;
    }

    // Obtener turno por ID
    public Turno obtenerPorId(Long id) {
        return turnoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Turno no encontrado con ID: " + id));
    }

    // Crear nuevo turno
    public Turno crearTurno(Turno turno) {
        return turnoRepository.save(turno);
    }

    // Actualizar turno existente
    public Turno actualizarTurno(Long id, Turno turnoActualizado) {
        Turno turno = obtenerPorId(id);
        turno.setFechaHora(turnoActualizado.getFechaHora());
        turno.setDuracion(turnoActualizado.getDuracion());
        turno.setEstado(turnoActualizado.getEstado());
        turno.setObservaciones(turnoActualizado.getObservaciones());
        return turnoRepository.save(turno);
    }

    // Eliminar turno (cancelar)
    public void eliminarTurno(Long id) {
        Turno turno = obtenerPorId(id);
        turnoRepository.delete(turno);
    }

    // Buscar turnos por profesional
    public List<Turno> obtenerTurnosPorProfesional(Integer profesionalId) {
        Persona profesional = validarPersona(profesionalId);
        return turnoRepository.findByProfesional(profesional);
    }

    // Buscar turnos por estado
    public List<Turno> obtenerTurnosPorEstado(Integer estadoId) {
        Estado estado = estadoRepository.findById(estadoId.longValue())
                .orElseThrow(() -> new RecursoNoEncontradoException("Estado no encontrado con ID: " + estadoId));

        List<Turno> turnos = turnoRepository.findByEstado(estado);
        if (turnos.isEmpty()) {
            throw new RecursoNoEncontradoException("No se encontraron turnos para el estado con ID: " + estadoId);
        }
        return turnos;
    }


    // Buscar turnos por fecha
    public List<Turno> obtenerTurnosPorFecha(LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(LocalTime.MAX);
        return turnoRepository.findByFechaHoraBetween(inicio, fin);
    }


    // Control de disponibilidad por profesional, especialidad y fecha
    public List<Turno> obtenerTurnosDeProfesionalEnFecha(Integer profesionalId, LocalDate fecha) {
        Persona profesional = validarPersona(profesionalId);
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(LocalTime.MAX);
        return turnoRepository.findByProfesionalAndFechaHoraBetween(profesional, inicio, fin);
    }

    public ReporteTurnosAtendidosDTO obtenerCantidadTurnosAtendidosPorProfesionalEnRango(Integer profesionalId, LocalDate fechaInicio, LocalDate fechaFin) {
        // Validar persona profesional
        Persona profesional = validarPersona(profesionalId);

        // Buscar estado "Atendido" (debes usar el nombre exacto que usas en BD)
        Estado estadoAtendido = estadoRepository.findByNombre("Atendido")
                .orElseThrow(() -> new RecursoNoEncontradoException("Estado 'Atendido' no encontrado"));

        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);

        long cantidad = turnoRepository.countByProfesionalAndEstadoAndFechaHoraBetween(profesional, estadoAtendido, inicio, fin);

        return new ReporteTurnosAtendidosDTO(Math.toIntExact(profesional.getId()), profesional.getNombre(), cantidad);
    }

    // ==== Métodos auxiliares ====

    private void validarPaciente(Integer pacienteId) {
        pacienteRepository.findById(pacienteId.longValue())
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado con ID: " + pacienteId));
    }

    private Persona validarPersona(Integer personaId) {
        return personaRepository.findById((int) personaId.longValue())
                .orElseThrow(() -> new RecursoNoEncontradoException("Persona no encontrada con ID: " + personaId));
    }
}
