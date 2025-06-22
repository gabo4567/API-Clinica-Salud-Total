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
import java.time.format.DateTimeFormatter;
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

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyyMMdd");

    public String generarNuevoComprobante(LocalDate fecha) {
        String fechaStr = fecha.format(FORMATO_FECHA);
        String prefix = "ST-" + fechaStr + "-";

        // Obtener el máximo comprobante que ya existe para ese día
        String maxComprobante = turnoRepository.findMaxComprobanteByPrefix(prefix);

        long siguienteNumero = 1; // default si no hay comprobantes aún

        if (maxComprobante != null) {
            // Extraer la parte numérica del comprobante
            String numeroStr = maxComprobante.substring(prefix.length());
            try {
                long numero = Long.parseLong(numeroStr);
                siguienteNumero = numero + 1;
            } catch (NumberFormatException e) {
                // En caso de error, seguir con 1 o loggear
            }
        }

        // Formatear número con ceros a la izquierda (6 dígitos)
        String numeroFormateado = String.format("%06d", siguienteNumero);

        return prefix + numeroFormateado;
    }



    // Obtener todos los turnos
    public List<TurnoDTO> listarTodos() {
        List<Turno> turnos = turnoRepository.findAll();
        return turnos.stream().map(this::convertirADTO).toList();
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

        Persona paciente = new Persona();
        paciente.setId(turnoDTO.getIdPaciente());
        System.out.println(">> Paciente seteado en turno: " + paciente.getId());
        turno.setPaciente(paciente);

        Persona profesional = new Persona();
        profesional.setId(turnoDTO.getIdProfesional());
        System.out.println(">> Profesional seteado en turno: " + profesional.getId());
        turno.setProfesional(profesional);

        Estado estado = new Estado();
        estado.setIdEstado(turnoDTO.getIdEstado());
        System.out.println(">> Estado seteado en turno: " + estado.getIdEstado());
        turno.setEstado(estado);

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

    public TurnoDTO crearTurno(TurnoDTO turnoDTO) {
        System.out.println(">> Crear turno - recibo DTO: " + turnoDTO);
        String nuevoComprobante = generarNuevoComprobante(LocalDate.now());
        turnoDTO.setComprobante(nuevoComprobante);

        Turno turno = convertirADominio(turnoDTO);
        System.out.println(">> Turno convertido a dominio: " + turno);

        Turno guardado = null;
        try {
            guardado = turnoRepository.save(turno);
        } catch (Exception e) {
            System.err.println(">> Error guardando turno: " + e.getMessage());
            e.printStackTrace();
            throw e;  // Re-lanzar para que el controller lo capture
        }
        System.out.println(">> Turno guardado con ID: " + guardado.getId());
        return convertirADTO(guardado);
    }


    // Actualizar turno existente (sin usar obtenerPorId)
    public TurnoDTO actualizarTurno(Long id, TurnoDTO turnoDTO) {
        System.out.println("DEBUG - ID recibido en TurnoService: " + id);
        System.out.println("DEBUG - DTO recibido: " + turnoDTO);

        Turno turno = new Turno();
        turno.setId(id);  // Se usa el ID directamente desde la URL

        // Seteamos los datos recibidos en el DTO
        turno.setFechaHora(turnoDTO.getFechaHora());
        turno.setDuracion(turnoDTO.getDuracion());

        // DEBUG del ID del estado
        System.out.println("DEBUG - ID del estado recibido: " + turnoDTO.getIdEstado());

        // Manejo de posible error al buscar el Estado
        Estado estado;
        try {
            estado = estadoRepository.findById(turnoDTO.getIdEstado())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Estado no encontrado con ID: " + turnoDTO.getIdEstado()));
        } catch (Exception e) {
            System.out.println("ERROR al obtener Estado desde el DTO: " + e.getMessage());
            throw e;
        }

        turno.setEstado(estado);
        turno.setObservaciones(turnoDTO.getObservaciones());

        // Asociar paciente
        Persona paciente = new Persona();
        paciente.setId(turnoDTO.getIdPaciente());
        turno.setPaciente(paciente);

        // Asociar profesional
        Persona profesional = new Persona();
        profesional.setId(turnoDTO.getIdProfesional());
        turno.setProfesional(profesional);

        // Comprobante
        String nuevoComprobante = generarNuevoComprobante(LocalDate.now());
        turno.setComprobante(nuevoComprobante);

        // Imprimir información antes de guardar
        System.out.println("DEBUG - Turno.id: " + turno.getId());
        System.out.println("DEBUG - Paciente.id: " + turno.getPaciente().getId());
        System.out.println("DEBUG - Profesional.id: " + turno.getProfesional().getId());
        System.out.println("DEBUG - Estado.id: " + turno.getEstado().getIdEstado());

        // Guardamos
        Turno actualizado = turnoRepository.save(turno);

        System.out.println("DEBUG - Turno actualizado: " + actualizado);
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


