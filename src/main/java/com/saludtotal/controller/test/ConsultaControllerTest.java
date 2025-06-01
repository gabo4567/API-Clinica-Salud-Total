package com.saludtotal.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saludtotal.controller.ConsultaController;
import com.saludtotal.dto.ConsultaRequestDTO;
import com.saludtotal.dto.ConsultaResponseDTO;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.service.ConsultaService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConsultaController.class)
public class ConsultaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsultaService consultaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearConsulta_Exitoso() throws Exception {
        ConsultaRequestDTO requestDTO = new ConsultaRequestDTO("Juan Pérez", "juan.perez@gmail.com", "Consulta de ejemplo", 1);
        ConsultaResponseDTO responseDTO = new ConsultaResponseDTO(1L, "Juan Pérez", "juan.perez@gmail.com", "Consulta de ejemplo", LocalDateTime.now(), "Activo");

        when(consultaService.crearConsulta(ArgumentMatchers.any())).thenReturn(responseDTO);

        mockMvc.perform(post("/api/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())  // 201
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$.correo", is("juan.perez@gmail.com")))
                .andExpect(jsonPath("$.mensaje", is("Consulta de ejemplo")));
    }

    @Test
    void listarConsultas_Exitoso() throws Exception {
        ConsultaResponseDTO consulta1 = new ConsultaResponseDTO(1L, "Juan Pérez", "juan.perez@gmail.com", "Consulta 1", LocalDateTime.now(), "Activo");
        ConsultaResponseDTO consulta2 = new ConsultaResponseDTO(2L, "Ana García", "ana.garcia@gmail.com", "Consulta 2", LocalDateTime.now(), "Activo");
        List<ConsultaResponseDTO> lista = Arrays.asList(consulta1, consulta2);

        when(consultaService.listarConsultas()).thenReturn(lista);

        mockMvc.perform(get("/api/consultas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$[1].nombre", is("Ana García")));
    }

    @Test
    void obtenerConsultaPorId_Exitoso() throws Exception {
        ConsultaResponseDTO consulta = new ConsultaResponseDTO(1L, "Juan Pérez", "juan.perez@gmail.com", "Consulta 1", LocalDateTime.now(), "Activo");
        when(consultaService.obtenerConsultaPorId(1L)).thenReturn(consulta);

        mockMvc.perform(get("/api/consultas/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$.mensaje", is("Consulta 1")));
    }

    @Test
    void eliminarConsulta_Exitoso() throws Exception {
        mockMvc.perform(delete("/api/consultas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void obtenerConsultaPorId_NoExiste() throws Exception {
        when(consultaService.obtenerConsultaPorId(99L))
                .thenThrow(new RecursoNoEncontradoException("Consulta con ID 99 no encontrada"));

        mockMvc.perform(get("/api/consultas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearConsulta_CorreoInvalido() throws Exception {
        ConsultaRequestDTO requestDTO = new ConsultaRequestDTO("Juan Pérez", "correo-invalido", "Mensaje válido", 1);

        mockMvc.perform(post("/api/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearConsulta_MensajeVacio() throws Exception {
        ConsultaRequestDTO requestDTO = new ConsultaRequestDTO("Juan Pérez", "juan.perez@gmail.com", "", 1);

        mockMvc.perform(post("/api/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }
}
