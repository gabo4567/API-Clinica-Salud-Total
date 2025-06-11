package com.saludtotal.service;

import com.saludtotal.clinica.models.*;
import com.saludtotal.dto.ReporteTurnosAtendidosDTO;
import com.saludtotal.dto.ReporteTurnosCanceladosYReprogramadosDTO;
import com.saludtotal.dto.TasaCancelacionPorEspecialidadDTO;
import com.saludtotal.dto.TurnoDTO;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.repositories.TurnoRepository;
import com.saludtotal.repositories.PacienteRepository;
import com.saludtotal.repositories.PersonaRepository;
import com.saludtotal.repositories.EstadoRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    // Obtener turno por ID (entidad)
    public Turno obtenerPorId(Long id) {
        return turnoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Turno no encontrado con ID: " + id));
    }

    // Obtener turno por ID (DTO)
    public TurnoDTO obtenerDTOporId(Long id) {
        Turno turno = obtenerPorId(id);
        return convertirADTO(turno);
    }

    // Metodo para convertir entidad Turno a TurnoDTO
    private TurnoDTO convertirADTO(Turno turno) {
        TurnoDTO dto = new TurnoDTO();
        dto.setId(turno.getId());
        dto.setComprobante(turno.getComprobante());
        dto.setIdPaciente(turno.getPaciente().getId());
        dto.setIdProfesional(turno.getProfesional().getId());
        dto.setFechaHora(turno.getFechaHora());
        dto.setDuracion(turno.getDuracion());
        dto.setIdEstado(turno.getEstado().getIdEstado());
        dto.setObservaciones(turno.getObservaciones());
        return dto;
    }

    // Metodo para convertir TurnoDTO a entidad Turno
    private Turno convertirADominio(TurnoDTO turnoDTO) {
        Turno turno = new Turno();
        turno.setId(turnoDTO.getId());
        turno.setComprobante(turnoDTO.getComprobante());

        turno.setPaciente(new Persona());
        turno.getPaciente().setId(turnoDTO.getIdPaciente());

        turno.setProfesional(new Persona());
        turno.getProfesional().setId(turnoDTO.getIdProfesional());

        turno.setEstado(new Estado());
        turno.getEstado().setIdEstado(turnoDTO.getIdEstado());

        turno.setFechaHora(turnoDTO.getFechaHora());
        turno.setDuracion(turnoDTO.getDuracion());
        turno.setObservaciones(turnoDTO.getObservaciones());

        return turno;
    }

    // Obtener turnos futuros por paciente, devolviendo lista DTO
    public List<TurnoDTO> obtenerTurnosFuturosPorPaciente(Long pacienteId) {
        validarPaciente(pacienteId);
        List<Turno> turnos = turnoRepository.findByPacienteIdAndFechaHoraAfter(pacienteId, LocalDateTime.now());
        if (turnos.isEmpty()) {
            throw new RecursoNoEncontradoException("No se encontraron turnos futuros para el paciente con ID: " + pacienteId);
        }
        return turnos.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Similar para turnos pasados
    public List<TurnoDTO> obtenerTurnosPasadosPorPaciente(Long pacienteId) {
        validarPaciente(pacienteId);
        List<Turno> turnos = turnoRepository.findByPacienteIdAndFechaHoraBefore(pacienteId, LocalDateTime.now());
        if (turnos.isEmpty()) {
            throw new RecursoNoEncontradoException("No se encontraron turnos pasados para el paciente con ID: " + pacienteId);
        }
        return turnos.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Crear nuevo turno
    public TurnoDTO crearTurno(TurnoDTO turnoDTO) {
        Turno turno = convertirADominio(turnoDTO); // metodo para convertir DTO a entidad
        Turno guardado = turnoRepository.save(turno);
        return convertirADTO(guardado); // metodo para convertir entidad a DTO
    }

    // Actualizar turno existente
    public TurnoDTO actualizarTurno(Long id, TurnoDTO turnoDTO) {
        Turno turno = obtenerPorId(id);
        turno.setFechaHora(turnoDTO.getFechaHora());
        turno.setDuracion(turnoDTO.getDuracion());
        // setear estado y observaciones con valores del DTO
        // Suponiendo que tienes un metodo para obtener Estado por id:
        Estado estado = estadoRepository.findById(turnoDTO.getIdEstado())
                .orElseThrow(() -> new RecursoNoEncontradoException("Estado no encontrado"));
        turno.setEstado(estado);
        turno.setObservaciones(turnoDTO.getObservaciones());

        Turno actualizado = turnoRepository.save(turno);
        return convertirADTO(actualizado);
    }

    // Eliminar turno (cancelar)
    public void eliminarTurno(Long id) {
        Turno turno = obtenerPorId(id);
        turnoRepository.delete(turno);
    }

    // Obtener turnos por profesional
    public List<TurnoDTO> obtenerTurnosPorProfesional(Long profesionalId) {
        Persona profesional = validarPersona(profesionalId);
        List<Turno> turnos = turnoRepository.findByProfesional(profesional);
        if (turnos.isEmpty()) {
            throw new RecursoNoEncontradoException("No se encontraron turnos para el profesional con ID: " + profesionalId);
        }
        return turnos.stream()
                .map(this::convertirADTO)
                .toList();
    }

    // Obtener turnos por estado
    public List<TurnoDTO> obtenerTurnosPorEstado(Long estadoId) {
        Estado estado = estadoRepository.findById(estadoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estado no encontrado con ID: " + estadoId));

        List<Turno> turnos = turnoRepository.findByEstado(estado);
        if (turnos.isEmpty()) {
            throw new RecursoNoEncontradoException("No se encontraron turnos para el estado con ID: " + estadoId);
        }
        return turnos.stream()
                .map(this::convertirADTO)
                .toList();
    }

    // Buscar turnos por fecha
    public List<TurnoDTO> obtenerTurnosPorFecha(LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(LocalTime.MAX);
        return turnoRepository.findByFechaHoraBetween(inicio, fin)
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    // Control de disponibilidad por profesional y fecha
    public List<TurnoDTO> obtenerTurnosDeProfesionalEnFecha(Long profesionalId, LocalDate fecha) {
        Persona profesional = validarPersona(profesionalId);
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(LocalTime.MAX);
        return turnoRepository.findByProfesionalAndFechaHoraBetween(profesional, inicio, fin)
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    // Reporte cantidad de turnos atendidos por profesional en rango
    public ReporteTurnosAtendidosDTO obtenerCantidadTurnosAtendidosPorProfesionalEnRango(Long profesionalId, LocalDate fechaInicio, LocalDate fechaFin) {
        Persona profesional = validarPersona(profesionalId);

        Estado estadoAtendido = estadoRepository.findByNombre("Atendido")
                .orElseThrow(() -> new RecursoNoEncontradoException("Estado 'Atendido' no encontrado"));

        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);

        long cantidad = turnoRepository.countByProfesionalAndEstadoAndFechaHoraBetween(profesional, estadoAtendido, inicio, fin);

        return new ReporteTurnosAtendidosDTO((long) Math.toIntExact(profesional.getId()), profesional.getNombre(), cantidad);
    }

    // Reporte de turnos cancelados y reprogramados
    public ReporteTurnosCanceladosYReprogramadosDTO obtenerReporteCanceladosYReprogramados(LocalDate fechaInicio, LocalDate fechaFin) {
        Estado estadoCancelado = estadoRepository.findByNombre("Cancelado")
                .orElseThrow(() -> new RecursoNoEncontradoException("Estado 'Cancelado' no encontrado"));

        Estado estadoReprogramado = estadoRepository.findByNombre("Reprogramado")
                .orElseThrow(() -> new RecursoNoEncontradoException("Estado 'Reprogramado' no encontrado"));

        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);

        long cancelados = turnoRepository.countByEstadoAndFechaHoraBetween(estadoCancelado, inicio, fin);
        long reprogramados = turnoRepository.countByEstadoAndFechaHoraBetween(estadoReprogramado, inicio, fin);

        return new ReporteTurnosCanceladosYReprogramadosDTO(cancelados, reprogramados);
    }

    // Tasa de cancelación de turnos por especialidad
    public List<TasaCancelacionPorEspecialidadDTO> obtenerTasaCancelacionPorEspecialidad(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Object[]> resultados = turnoRepository.obtenerDatosCancelacionPorEspecialidad(fechaInicio, fechaFin);

        List<TasaCancelacionPorEspecialidadDTO> listaDTO = new ArrayList<>();
        for (Object[] fila : resultados) {
            String especialidad = (String) fila[0];
            long totalTurnos = ((Number) fila[1]).longValue();
            long turnosCancelados = ((Number) fila[2]).longValue();

            double tasaCancelacion = totalTurnos > 0 ? (double) turnosCancelados / totalTurnos * 100 : 0.0;

            TasaCancelacionPorEspecialidadDTO dto = new TasaCancelacionPorEspecialidadDTO();
            dto.setEspecialidad(especialidad);
            dto.setTotalTurnos(totalTurnos);
            dto.setTurnosCancelados(turnosCancelados);
            dto.setTasaCancelacion(tasaCancelacion);

            listaDTO.add(dto);
        }
        return listaDTO;
    }




    // ==== Métodos auxiliares ====

    private void validarPaciente(Long pacienteId) {
        pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado con ID: " + pacienteId));
    }

    private Persona validarPersona(Long personaId) {
        return personaRepository.findById(personaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Persona no encontrada con ID: " + personaId));
    }
}


