package com.saludtotal.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saludtotal.clinica.models.Turno;
import com.saludtotal.controller.TurnoController;
import com.saludtotal.dto.ReporteTurnosAtendidosDTO;
import com.saludtotal.dto.ReporteTurnosCanceladosYReprogramadosDTO;
import com.saludtotal.dto.TasaCancelacionPorEspecialidadDTO;
import com.saludtotal.exceptions.GlobalExceptionHandler;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.service.TurnoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TurnoController.class)
@Import(GlobalExceptionHandler.class)
class TurnoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TurnoService turnoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/turnos/paciente/{id}/futuros - Éxito")
    void testGetTurnosFuturosPorPaciente() throws Exception {
        Turno turno = new Turno();
        turno.setId(1);
        turno.setFechaHora(LocalDateTime.now().plusDays(2));
        when(turnoService.obtenerTurnosFuturosPorPaciente(1)).thenReturn(List.of(turno));

        mockMvc.perform(get("/api/turnos/paciente/1/futuros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @DisplayName("GET /api/turnos/paciente/{id}/futuros - Paciente no encontrado")
    void testGetTurnosFuturosPorPacienteNotFound() throws Exception {
        when(turnoService.obtenerTurnosFuturosPorPaciente(99))
                .thenThrow(new RecursoNoEncontradoException("Paciente no encontrado con ID: 99"));

        mockMvc.perform(get("/api/turnos/paciente/99/futuros"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Paciente no encontrado con ID: 99"));
    }

    @Test
    @DisplayName("GET /api/turnos/paciente/{id}/pasados - Éxito")
    void testGetTurnosPasadosPorPaciente() throws Exception {
        Turno turno = new Turno();
        turno.setId(10);
        turno.setFechaHora(LocalDateTime.now().minusDays(2));
        when(turnoService.obtenerTurnosPasadosPorPaciente(1)).thenReturn(List.of(turno));

        mockMvc.perform(get("/api/turnos/paciente/1/pasados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L));
    }

    @Test
    @DisplayName("GET /api/turnos/paciente/{id}/pasados - Paciente no encontrado")
    void testGetTurnosPasadosPorPacienteNotFound() throws Exception {
        when(turnoService.obtenerTurnosPasadosPorPaciente(99))
                .thenThrow(new RecursoNoEncontradoException("Paciente no encontrado con ID: 99"));

        mockMvc.perform(get("/api/turnos/paciente/99/pasados"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Paciente no encontrado con ID: 99"));
    }

    @Test
    @DisplayName("POST /api/turnos - Crear turno")
    void testCrearTurno() throws Exception {
        Turno turno = new Turno();
        turno.setId(1);
        turno.setFechaHora(LocalDateTime.now().plusDays(1));
        turno.setDuracion(30);
        when(turnoService.crearTurno(any(Turno.class))).thenReturn(turno);

        mockMvc.perform(post("/api/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(turno)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("PUT /api/turnos/{id} - Actualizar turno")
    void testActualizarTurno() throws Exception {
        Turno actualizado = new Turno();
        actualizado.setId(4);
        actualizado.setDuracion(40);
        when(turnoService.actualizarTurno(eq(4L), any(Turno.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/turnos/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.duracion").value(40));
    }

    @Test
    @DisplayName("PUT /api/turnos/{id} - Turno no encontrado")
    void testActualizarTurnoNotFound() throws Exception {
        Turno turno = new Turno();
        turno.setDuracion(40);
        when(turnoService.actualizarTurno(eq(77L), any(Turno.class)))
                .thenThrow(new RecursoNoEncontradoException("Turno no encontrado con ID: 77"));

        mockMvc.perform(put("/api/turnos/77")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(turno)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Turno no encontrado con ID: 77"));
    }

    @Test
    @DisplayName("DELETE /api/turnos/{id} - Éxito")
    void testEliminarTurno() throws Exception {
        mockMvc.perform(delete("/api/turnos/3"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/turnos/{id} - Turno no encontrado")
    void testEliminarTurnoNotFound() throws Exception {
        Mockito.doThrow(new RecursoNoEncontradoException("Turno no encontrado con ID: 88"))
                .when(turnoService).eliminarTurno(88L);

        mockMvc.perform(delete("/api/turnos/88"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Turno no encontrado con ID: 88"));
    }

    @Test
    @DisplayName("GET /api/turnos/{id} - Turno encontrado")
    void testGetTurnoPorId() throws Exception {
        Turno turno = new Turno();
        turno.setId(5);
        turno.setDuracion(20);
        when(turnoService.obtenerPorId(5L)).thenReturn(turno);

        mockMvc.perform(get("/api/turnos/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L));
    }

    @Test
    @DisplayName("GET /api/turnos/{id} - Turno no encontrado")
    void testGetTurnoPorIdNotFound() throws Exception {
        when(turnoService.obtenerPorId(77L))
                .thenThrow(new RecursoNoEncontradoException("Turno no encontrado con ID: 77"));

        mockMvc.perform(get("/api/turnos/77"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Turno no encontrado con ID: 77"));
    }

    @Test
    @DisplayName("GET /api/turnos/estado/{id} - Turnos por estado")
    void testGetTurnosPorEstado() throws Exception {
        Turno turno = new Turno();
        turno.setId(8);
        when(turnoService.obtenerTurnosPorEstado(2)).thenReturn(List.of(turno));

        mockMvc.perform(get("/api/turnos/estado/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(8L));
    }

    @Test
    @DisplayName("GET /api/turnos/estado/{id} - Estado no encontrado")
    void testGetTurnosPorEstadoNotFound() throws Exception {
        when(turnoService.obtenerTurnosPorEstado(99))
                .thenThrow(new RecursoNoEncontradoException("Estado no encontrado con ID: 99"));

        mockMvc.perform(get("/api/turnos/estado/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Estado no encontrado con ID: 99"));
    }

    @Test
    @DisplayName("GET /api/turnos/fecha?fecha=2025-06-15 - Turnos por fecha")
    void testGetTurnosPorFecha() throws Exception {
        Turno turno = new Turno();
        turno.setId(6);
        when(turnoService.obtenerTurnosPorFecha(LocalDate.of(2025, 6, 15))).thenReturn(List.of(turno));

        mockMvc.perform(get("/api/turnos/fecha?fecha=2025-06-15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(6L));
    }

    @Test
    @DisplayName("GET /api/turnos/profesional/{id}/disponibilidad - Turnos por profesional y fecha")
    void testGetTurnosPorProfesionalEnFecha() throws Exception {
        Turno turno = new Turno();
        turno.setId(9);
        when(turnoService.obtenerTurnosDeProfesionalEnFecha(eq(5), eq(LocalDate.of(2025, 6, 15))))
                .thenReturn(List.of(turno));

        mockMvc.perform(get("/api/turnos/profesional/5/disponibilidad?fecha=2025-06-15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(9L));
    }

    @Test
    void testGetCantidadTurnosAtendidosPorProfesional_Exitoso() throws Exception {
        Integer profesionalId = 1;
        String fechaInicio = "2024-01-01";
        String fechaFin = "2024-01-31";

        ReporteTurnosAtendidosDTO dto = new ReporteTurnosAtendidosDTO(profesionalId, "Dr. López", 5);

        when(turnoService.obtenerCantidadTurnosAtendidosPorProfesionalEnRango(
                profesionalId, LocalDate.parse(fechaInicio), LocalDate.parse(fechaFin)))
                .thenReturn(dto);

        mockMvc.perform(get("/api/turnos/reportes/turnos-atendidos")
                        .param("profesionalId", profesionalId.toString())
                        .param("fechaInicio", fechaInicio)
                        .param("fechaFin", fechaFin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProfesional").value(1))
                .andExpect(jsonPath("$.nombreProfesional").value("Dr. López"))
                .andExpect(jsonPath("$.cantidadTurnosAtendidos").value(5));
    }


    @Test
    void testGetCantidadTurnosAtendidosPorProfesional_ProfesionalNoEncontrado() throws Exception {
        Integer profesionalId = 1;
        String fechaInicio = "2024-01-01";
        String fechaFin = "2024-01-31";

        when(turnoService.obtenerCantidadTurnosAtendidosPorProfesionalEnRango(profesionalId, LocalDate.parse(fechaInicio), LocalDate.parse(fechaFin)))
                .thenThrow(new RecursoNoEncontradoException("Persona no encontrada con ID: 1"));

        mockMvc.perform(get("/api/turnos/reportes/turnos-atendidos")
                        .param("profesionalId", profesionalId.toString())
                        .param("fechaInicio", fechaInicio)
                        .param("fechaFin", fechaFin))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Persona no encontrada con ID: 1"));
    }

    @Test
    void testGetCantidadTurnosAtendidosPorProfesional_EstadoNoEncontrado() throws Exception {
        Integer profesionalId = 1;
        String fechaInicio = "2024-01-01";
        String fechaFin = "2024-01-31";

        when(turnoService.obtenerCantidadTurnosAtendidosPorProfesionalEnRango(profesionalId, LocalDate.parse(fechaInicio), LocalDate.parse(fechaFin)))
                .thenThrow(new RecursoNoEncontradoException("Estado 'Atendido' no encontrado"));

        mockMvc.perform(get("/api/turnos/reportes/turnos-atendidos")
                        .param("profesionalId", profesionalId.toString())
                        .param("fechaInicio", fechaInicio)
                        .param("fechaFin", fechaFin))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Estado 'Atendido' no encontrado"));
    }

    @Test
    void testGetCantidadTurnosCanceladosYReprogramados_Exitoso() throws Exception {
        String fechaInicio = "2024-01-01";
        String fechaFin = "2024-01-31";

        ReporteTurnosCanceladosYReprogramadosDTO dto = new ReporteTurnosCanceladosYReprogramadosDTO(3, 2);

        when(turnoService.obtenerReporteCanceladosYReprogramados(
                LocalDate.parse(fechaInicio), LocalDate.parse(fechaFin)))
                .thenReturn(dto);

        mockMvc.perform(get("/api/turnos/reportes/turnos-cancelados-reprogramados")
                        .param("fechaInicio", fechaInicio)
                        .param("fechaFin", fechaFin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidadCancelados").value(3))
                .andExpect(jsonPath("$.cantidadReprogramados").value(2));
    }

    @Test
    void testGetCantidadTurnosCanceladosYReprogramados_EstadoNoEncontrado() throws Exception {
        String fechaInicio = "2024-01-01";
        String fechaFin = "2024-01-31";

        when(turnoService.obtenerReporteCanceladosYReprogramados(
                LocalDate.parse(fechaInicio), LocalDate.parse(fechaFin)))
                .thenThrow(new RecursoNoEncontradoException("Estado 'Cancelado' no encontrado"));

        mockMvc.perform(get("/api/turnos/reportes/turnos-cancelados-reprogramados")
                        .param("fechaInicio", fechaInicio)
                        .param("fechaFin", fechaFin))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Estado 'Cancelado' no encontrado"));
    }

    @Test
    void testObtenerTasaCancelacionPorEspecialidad_Exitoso() throws Exception {
        String fechaInicio = "2025-01-01";
        String fechaFin = "2025-12-31";

        TasaCancelacionPorEspecialidadDTO dto1 = new TasaCancelacionPorEspecialidadDTO();
        dto1.setEspecialidad("Pediatría");
        dto1.setTasaCancelacion(30.0);

        TasaCancelacionPorEspecialidadDTO dto2 = new TasaCancelacionPorEspecialidadDTO();
        dto2.setEspecialidad("Cardiología");
        dto2.setTasaCancelacion(10.0);

        List<TasaCancelacionPorEspecialidadDTO> mockLista = Arrays.asList(dto1, dto2);

        when(turnoService.obtenerTasaCancelacionPorEspecialidad(LocalDate.parse(fechaInicio), LocalDate.parse(fechaFin)))
                .thenReturn(mockLista);

        mockMvc.perform(get("/api/turnos/reportes/tasa-cancelacion-por-especialidad")
                        .param("fechaInicio", fechaInicio)
                        .param("fechaFin", fechaFin))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].especialidad").value("Pediatría"))
                .andExpect(jsonPath("$[0].tasaCancelacion").value(30.0))
                .andExpect(jsonPath("$[1].especialidad").value("Cardiología"))
                .andExpect(jsonPath("$[1].tasaCancelacion").value(10.0));
    }

    @Test
    void testObtenerTasaCancelacionPorEspecialidad_NoExitoso_SinResultados() throws Exception {
        LocalDate fechaInicio = LocalDate.of(2025, 1, 1);
        LocalDate fechaFin = LocalDate.of(2025, 12, 31);

        // Simular que no hay datos para ese rango
        when(turnoService.obtenerTasaCancelacionPorEspecialidad(fechaInicio, fechaFin))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/turnos/reportes/tasa-cancelacion-por-especialidad")
                        .param("fechaInicio", fechaInicio.toString())
                        .param("fechaFin", fechaFin.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }




}
