// Paquete y imports sin cambios
package com.saludtotal.service.test;

import com.saludtotal.clinica.models.*;
import com.saludtotal.dto.ReporteTurnosAtendidosDTO;
import com.saludtotal.dto.ReporteTurnosCanceladosYReprogramadosDTO;
import com.saludtotal.dto.TasaCancelacionPorEspecialidadDTO;
import com.saludtotal.dto.TurnoDTO;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.repositories.*;
import com.saludtotal.service.TurnoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TurnoServiceTest {

    @Mock
    private TurnoRepository turnoRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private EstadoRepository estadoRepository;

    @InjectMocks
    private TurnoService turnoService;

    private Paciente paciente;
    private Persona profesional;
    private Estado estado;
    private Turno turno;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Persona personaPaciente = new Persona();
        personaPaciente.setId(1L);

        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setPersona(personaPaciente);

        profesional = new Persona();
        profesional.setId(2L);

        estado = new Estado();
        estado.setIdEstado(1L);

        turno = new Turno();
        turno.setId(1L);
        turno.setPaciente(personaPaciente);
        turno.setProfesional(profesional);
        turno.setEstado(estado);
        turno.setFechaHora(LocalDateTime.now().plusDays(1));
    }


    @Test
    void obtenerTurnosFuturosPorPaciente_sinTurnos() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(turnoRepository.findByPacienteIdAndFechaHoraAfter(eq(1L), any(LocalDateTime.class))).thenReturn(Collections.emptyList());

        assertThrows(RecursoNoEncontradoException.class, () -> turnoService.obtenerTurnosFuturosPorPaciente(1L));
    }

    @Test
    void obtenerTurnosFuturosPorPaciente_conTurnos() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(turnoRepository.findByPacienteIdAndFechaHoraAfter(eq(1L), any(LocalDateTime.class))).thenReturn(List.of(turno));

        List<TurnoDTO> result = turnoService.obtenerTurnosFuturosPorPaciente(1L);

        assertEquals(1, result.size());
        assertEquals(turno.getId(), result.get(0).getId());
        assertEquals(turno.getFechaHora(), result.get(0).getFechaHora());
        assertEquals(turno.getPaciente().getId(), result.get(0).getIdPaciente());
        assertEquals(turno.getProfesional().getId(), result.get(0).getIdProfesional());
    }

    @Test
    void obtenerTurnosPasadosPorPaciente_conTurnos() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(turnoRepository.findByPacienteIdAndFechaHoraBefore(eq(1L), any(LocalDateTime.class))).thenReturn(List.of(turno));

        List<TurnoDTO> result = turnoService.obtenerTurnosPasadosPorPaciente(1L);

        assertEquals(1, result.size());
        assertEquals(turno.getId(), result.get(0).getId());
        assertEquals(turno.getFechaHora(), result.get(0).getFechaHora());
        assertEquals(turno.getPaciente().getId(), result.get(0).getIdPaciente());
        assertEquals(turno.getProfesional().getId(), result.get(0).getIdProfesional());
    }

    @Test
    void obtenerPorId_existente() {
        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turno));
        Turno result = turnoService.obtenerPorId(1L);
        assertNotNull(result);
    }

    @Test
    void obtenerPorId_noExistente() {
        when(turnoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> turnoService.obtenerPorId(99L));
    }

    @Test
    void crearTurno() {
        when(turnoRepository.save(any(Turno.class))).thenReturn(turno);
        TurnoDTO turnoDTO = new TurnoDTO();
        turnoDTO.setId(turno.getId());
        turnoDTO.setIdPaciente(turno.getPaciente().getId());
        turnoDTO.setIdProfesional(turno.getProfesional().getId());
        turnoDTO.setIdEstado(turno.getEstado().getIdEstado());
        turnoDTO.setFechaHora(turno.getFechaHora());
        turnoDTO.setDuracion(turno.getDuracion());
        turnoDTO.setObservaciones(turno.getObservaciones());

        TurnoDTO result = turnoService.crearTurno(turnoDTO);

        assertEquals(turno.getId(), result.getId());
    }

    @Test
    void actualizarTurno_existente() {
        TurnoDTO actualizadoDTO = new TurnoDTO();
        actualizadoDTO.setFechaHora(LocalDateTime.now().plusDays(2));
        actualizadoDTO.setDuracion(30);
        actualizadoDTO.setIdEstado(estado.getIdEstado());
        actualizadoDTO.setObservaciones("obs");
        actualizadoDTO.setIdPaciente(turno.getPaciente().getId());
        actualizadoDTO.setIdProfesional(turno.getProfesional().getId());

        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turno));
        when(turnoRepository.save(any(Turno.class))).thenReturn(turno);

        TurnoDTO result = turnoService.actualizarTurno(1L, actualizadoDTO);

        assertNotNull(result);
        assertEquals(actualizadoDTO.getDuracion(), result.getDuracion());
    }


    @Test
    void eliminarTurno_existente() {
        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turno));
        turnoService.eliminarTurno(1L);
        verify(turnoRepository).delete(turno);
    }


    @Test
    void obtenerTurnosPorEstado_sinResultados() {
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));
        when(turnoRepository.findByEstado(estado)).thenReturn(Collections.emptyList());

        assertThrows(RecursoNoEncontradoException.class, () -> turnoService.obtenerTurnosPorEstado(1L));
    }

    @Test
    void testObtenerCantidadTurnosAtendidosPorProfesionalEnRango_Exitoso() {
        Long profesionalId = 1L;
        LocalDate fechaInicio = LocalDate.of(2024, 1, 1);
        LocalDate fechaFin = LocalDate.of(2024, 1, 31);

        Persona profesional = new Persona();
        profesional.setId(profesionalId);
        profesional.setNombre("Dr. López");

        Estado estadoAtendido = new Estado();
        estadoAtendido.setNombre("Atendido");

        long cantidadEsperada = 5;

        when(personaRepository.findById(profesionalId)).thenReturn(Optional.of(profesional));
        when(estadoRepository.findByNombre("Atendido")).thenReturn(Optional.of(estadoAtendido));
        when(turnoRepository.countByProfesionalAndEstadoAndFechaHoraBetween(
                eq(profesional), eq(estadoAtendido), any(), any()
        )).thenReturn(cantidadEsperada);

        ReporteTurnosAtendidosDTO resultado = turnoService.obtenerCantidadTurnosAtendidosPorProfesionalEnRango(profesionalId, fechaInicio, fechaFin);

        assertEquals(profesionalId, resultado.getIdProfesional());
        assertEquals("Dr. López", resultado.getNombreProfesional());
        assertEquals(5, resultado.getCantidadTurnosAtendidos());
    }

    @Test
    void testObtenerCantidadTurnosAtendidosPorProfesionalEnRango_ProfesionalNoEncontrado() {
        Long profesionalId = 1L;
        LocalDate fechaInicio = LocalDate.of(2024, 1, 1);
        LocalDate fechaFin = LocalDate.of(2024, 1, 31);

        when(personaRepository.findById(profesionalId)).thenReturn(Optional.empty());

        RecursoNoEncontradoException ex = assertThrows(
                RecursoNoEncontradoException.class,
                () -> turnoService.obtenerCantidadTurnosAtendidosPorProfesionalEnRango(profesionalId, fechaInicio, fechaFin)
        );

        assertEquals("Persona no encontrada con ID: 1", ex.getMessage());
    }

    @Test
    void testObtenerCantidadTurnosAtendidosPorProfesionalEnRango_EstadoNoEncontrado() {
        Long profesionalId = 1L;
        LocalDate fechaInicio = LocalDate.of(2024, 1, 1);
        LocalDate fechaFin = LocalDate.of(2024, 1, 31);

        Persona profesional = new Persona();
        profesional.setId(profesionalId);
        profesional.setNombre("Dr. López");

        when(personaRepository.findById(profesionalId)).thenReturn(Optional.of(profesional));
        when(estadoRepository.findByNombre("Atendido")).thenReturn(Optional.empty());

        RecursoNoEncontradoException ex = assertThrows(
                RecursoNoEncontradoException.class,
                () -> turnoService.obtenerCantidadTurnosAtendidosPorProfesionalEnRango(profesionalId, fechaInicio, fechaFin)
        );

        assertEquals("Estado 'Atendido' no encontrado", ex.getMessage());
    }

    @Test
    void testObtenerCantidadTurnosCanceladosYReprogramadosEnPeriodo_Exitoso() {
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 1, 31);

        Estado estadoCancelado = new Estado();
        estadoCancelado.setIdEstado(10L);
        estadoCancelado.setNombre("Cancelado");

        Estado estadoReprogramado = new Estado();
        estadoReprogramado.setIdEstado(11L);
        estadoReprogramado.setNombre("Reprogramado");

        when(estadoRepository.findByNombre("Cancelado")).thenReturn(Optional.of(estadoCancelado));
        when(estadoRepository.findByNombre("Reprogramado")).thenReturn(Optional.of(estadoReprogramado));
        when(turnoRepository.countByEstadoAndFechaHoraBetween(eq(estadoCancelado), any(), any())).thenReturn(3L);
        when(turnoRepository.countByEstadoAndFechaHoraBetween(eq(estadoReprogramado), any(), any())).thenReturn(2L);

        ReporteTurnosCanceladosYReprogramadosDTO resultado = turnoService.obtenerReporteCanceladosYReprogramados(inicio, fin);

        assertEquals(3, resultado.getCantidadCancelados());
        assertEquals(2, resultado.getCantidadReprogramados());
    }

    @Test
    void testObtenerCantidadTurnosCanceladosYReprogramadosEnPeriodo_EstadoCanceladoNoEncontrado() {
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 1, 31);

        when(estadoRepository.findByNombre("Cancelado")).thenReturn(Optional.empty());

        RecursoNoEncontradoException exception = assertThrows(RecursoNoEncontradoException.class, () ->
                turnoService.obtenerReporteCanceladosYReprogramados(inicio, fin)
        );

        assertEquals("Estado 'Cancelado' no encontrado", exception.getMessage());
    }

    @Test
    void testObtenerCantidadTurnosCanceladosYReprogramadosEnPeriodo_EstadoReprogramadoNoEncontrado() {
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 1, 31);

        Estado estadoCancelado = new Estado();
        estadoCancelado.setIdEstado(10L);
        estadoCancelado.setNombre("Cancelado");

        when(estadoRepository.findByNombre("Cancelado")).thenReturn(Optional.of(estadoCancelado));
        when(estadoRepository.findByNombre("Reprogramado")).thenReturn(Optional.empty());

        RecursoNoEncontradoException exception = assertThrows(RecursoNoEncontradoException.class, () ->
                turnoService.obtenerReporteCanceladosYReprogramados(inicio, fin)
        );

        assertEquals("Estado 'Reprogramado' no encontrado", exception.getMessage());
    }

    @Test
    void obtenerTasaCancelacionPorEspecialidad_Exitoso() {
        LocalDate fechaInicioDate = LocalDate.of(2025, 1, 1);
        LocalDate fechaFinDate = LocalDate.of(2025, 12, 31);
        LocalDate fechaInicio = LocalDate.from(fechaInicioDate.atStartOfDay());
        LocalDateTime fechaFin = fechaFinDate.atTime(LocalTime.MAX);

        List<Object[]> listaSimulada = new ArrayList<>();
        listaSimulada.add(new Object[]{"Pediatría", 30.0});
        listaSimulada.add(new Object[]{"Cardiología", 10.0});

        when(turnoRepository.obtenerDatosCancelacionPorEspecialidad(fechaInicio, LocalDate.from(fechaFin)))
                .thenReturn(listaSimulada);

        List<TasaCancelacionPorEspecialidadDTO> resultado = turnoService.obtenerTasaCancelacionPorEspecialidad(fechaInicioDate, fechaFinDate);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Pediatría", resultado.get(0).getEspecialidad());
        assertEquals(30.0, resultado.get(0).getTasaCancelacion());
    }

    @Test
    void obtenerTasaCancelacionPorEspecialidad_SinResultados() {
        LocalDate fechaInicioDate = LocalDate.of(2025, 1, 1);
        LocalDate fechaFinDate = LocalDate.of(2025, 12, 31);
        LocalDate fechaInicio = LocalDate.from(fechaInicioDate.atStartOfDay());
        LocalDateTime fechaFin = fechaFinDate.atTime(LocalTime.MAX);

        when(turnoRepository.obtenerDatosCancelacionPorEspecialidad(fechaInicio, LocalDate.from(fechaFin)))
                .thenReturn(Collections.emptyList());

        List<TasaCancelacionPorEspecialidadDTO> resultado = turnoService.obtenerTasaCancelacionPorEspecialidad(fechaInicioDate, fechaFinDate);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(), "La lista de tasas de cancelación debería estar vacía");
    }
}

