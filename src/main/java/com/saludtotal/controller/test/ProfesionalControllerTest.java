package com.saludtotal.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saludtotal.controller.ProfesionalController;
import com.saludtotal.dto.ProfesionalDTO;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.service.ProfesionalService;
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

@WebMvcTest(ProfesionalController.class)
public class ProfesionalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfesionalService profesionalService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProfesionalDTO dto;

    @BeforeEach
    void setUp() {
        dto = new ProfesionalDTO();
        dto.setId(1L);
        dto.setIdPersona(10L);
        dto.setIdEspecialidad(3L);
        dto.setMatriculaProfesional("MP12345");
        dto.setIdEstado(1L);
    }

    @Test
    void testListarTodos() throws Exception {
        Mockito.when(profesionalService.listarTodos()).thenReturn(Arrays.asList(dto));

        mockMvc.perform(get("/api/profesionales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].matriculaProfesional", is("MP12345")));
    }

    @Test
    void testBuscarPorId_existente() throws Exception {
        Mockito.when(profesionalService.buscarPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/profesionales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.matriculaProfesional", is("MP12345")));
    }

    @Test
    void testBuscarPorId_noExistente() throws Exception {
        Mockito.when(profesionalService.buscarPorId(99L)).thenThrow(new RecursoNoEncontradoException("Profesional no encontrado"));

        mockMvc.perform(get("/api/profesionales/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Profesional no encontrado")));
    }

    @Test
    void testRegistrarProfesional() throws Exception {
        Mockito.when(profesionalService.registrarProfesional(any())).thenReturn(dto);

        mockMvc.perform(post("/api/profesionales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.matriculaProfesional", is("MP12345")));
    }

    @Test
    void testActualizarProfesional_existente() throws Exception {
        Mockito.when(profesionalService.actualizarProfesional(eq(1L), any())).thenReturn(dto);

        mockMvc.perform(put("/api/profesionales/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void testActualizarProfesional_noExistente() throws Exception {
        Mockito.when(profesionalService.actualizarProfesional(eq(99L), any()))
                .thenThrow(new RecursoNoEncontradoException("Profesional no encontrado"));

        mockMvc.perform(put("/api/profesionales/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Profesional no encontrado")));
    }

    @Test
    void testEliminarProfesional_existente() throws Exception {
        mockMvc.perform(delete("/api/profesionales/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarProfesional_noExistente() throws Exception {
        Mockito.doThrow(new RecursoNoEncontradoException("Profesional no encontrado"))
                .when(profesionalService).eliminarProfesional(99L);

        mockMvc.perform(delete("/api/profesionales/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Profesional no encontrado")));
    }
}
