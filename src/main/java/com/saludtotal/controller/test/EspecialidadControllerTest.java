package com.saludtotal.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saludtotal.controller.EspecialidadController;
import com.saludtotal.dto.EspecialidadDTO;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.service.EspecialidadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EspecialidadController.class)
public class EspecialidadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EspecialidadService especialidadService;

    @Autowired
    private ObjectMapper objectMapper;

    private EspecialidadDTO dto;

    @BeforeEach
    void setUp() {
        dto = new EspecialidadDTO();
        dto.setIdEspecialidad(1L);
        dto.setNombre("Cardiología");
        dto.setDescripcion("Tratamiento del corazón");
        dto.setIdEstado(1L);
    }

    @Test
    void testListarTodos() throws Exception {
        Mockito.when(especialidadService.listarTodos()).thenReturn(Arrays.asList(dto));

        mockMvc.perform(get("/api/especialidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEspecialidad", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Cardiología")));
    }

    @Test
    void testBuscarPorId_existente() throws Exception {
        Mockito.when(especialidadService.buscarPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/especialidades/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEspecialidad", is(1)))
                .andExpect(jsonPath("$.nombre", is("Cardiología")));
    }

    @Test
    void testBuscarPorId_noExistente() throws Exception {
        Mockito.when(especialidadService.buscarPorId(99L)).thenThrow(new RecursoNoEncontradoException("Especialidad no encontrada"));

        mockMvc.perform(get("/api/especialidades/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Especialidad no encontrada")));
    }

    @Test
    void testRegistrarEspecialidad() throws Exception {
        Mockito.when(especialidadService.registrarEspecialidad(any())).thenReturn(dto);

        mockMvc.perform(post("/api/especialidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEspecialidad", is(1)))
                .andExpect(jsonPath("$.nombre", is("Cardiología")));
    }

    @Test
    void testActualizarEspecialidad_existente() throws Exception {
        Mockito.when(especialidadService.actualizarEspecialidad(eq(1L), any())).thenReturn(dto);

        mockMvc.perform(put("/api/especialidades/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEspecialidad", is(1)));
    }

    @Test
    void testActualizarEspecialidad_noExistente() throws Exception {
        Mockito.when(especialidadService.actualizarEspecialidad(eq(99L), any()))
                .thenThrow(new RecursoNoEncontradoException("Especialidad no encontrada"));

        mockMvc.perform(put("/api/especialidades/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Especialidad no encontrada")));
    }

    @Test
    void testEliminarEspecialidad_existente() throws Exception {
        mockMvc.perform(delete("/api/especialidades/1"))
                .andExpect(status().isNoContent());  // 204
    }

    @Test
    void testEliminarEspecialidad_noExistente() throws Exception {
        Mockito.doThrow(new RecursoNoEncontradoException("Especialidad no encontrada"))
                .when(especialidadService).eliminarEspecialidad(99L);

        mockMvc.perform(delete("/api/especialidades/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Especialidad no encontrada")));
    }
}
