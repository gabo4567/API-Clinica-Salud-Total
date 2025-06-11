package com.saludtotal.service.test;

import com.saludtotal.dto.ConsultaRequestDTO;
import com.saludtotal.dto.ConsultaResponseDTO;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.clinica.models.Consulta;
import com.saludtotal.clinica.models.Estado;
import com.saludtotal.repositories.ConsultaRepository;
import com.saludtotal.repositories.EstadoRepository;
import com.saludtotal.service.ConsultaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ConsultaServiceTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private EstadoRepository estadoRepository;

    @InjectMocks
    private ConsultaService consultaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearConsulta_Exitoso() {
        ConsultaRequestDTO dto = new ConsultaRequestDTO("Juan Perez", "juan@mail.com", "Consulta sobre turnos", 1L);
        Estado estado = new Estado(1L, "Nuevo");

        Consulta consulta = new Consulta(1L, "Juan Perez", "juan@mail.com", "Consulta sobre turnos", estado);

        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consulta);

        ConsultaResponseDTO respuesta = consultaService.crearConsulta(dto);

        assertNotNull(respuesta);
        assertEquals("Juan Perez", respuesta.getNombre());
        assertEquals("juan@mail.com", respuesta.getCorreo());
    }


    @Test
    void crearConsulta_EstadoNoExiste() {
        ConsultaRequestDTO dto = new ConsultaRequestDTO("Ana", "ana@mail.com", "Mensaje", 99L);
        when(estadoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> {
            consultaService.crearConsulta(dto);
        });
    }

    @Test
    void listarConsultas_Exitoso() {
        Estado estado = new Estado(1L, "Nuevo");
        Consulta consulta1 = new Consulta(1L, "Juan", "juan@mail.com", "Mensaje 1", estado);
        Consulta consulta2 = new Consulta(2L, "Ana", "ana@mail.com", "Mensaje 2", estado);

        when(consultaRepository.findAll()).thenReturn(Arrays.asList(consulta1, consulta2));

        List<ConsultaResponseDTO> resultado = consultaService.listarConsultas();

        assertEquals(2, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
    }

    @Test
    void obtenerConsultaPorId_Exitoso() {
        Estado estado = new Estado(1L, "Nuevo");
        Consulta consulta = new Consulta(1L, "Juan", "juan@mail.com", "Mensaje 1", estado);

        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));

        ConsultaResponseDTO resultado = consultaService.obtenerConsultaPorId(1L);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
    }

    @Test
    void obtenerConsultaPorId_NoExiste() {
        when(consultaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> {
            consultaService.obtenerConsultaPorId(99L);
        });
    }

    @Test
    void eliminarConsulta_Exitoso() {
        Estado estado = new Estado(1L, "Nuevo");
        Consulta consulta = new Consulta(1L, "Juan", "juan@mail.com", "Mensaje 1", estado);

        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));

        consultaService.eliminarConsulta(1L);

        verify(consultaRepository, times(1)).delete(consulta);
    }

    @Test
    void eliminarConsulta_NoExiste() {
        when(consultaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> {
            consultaService.eliminarConsulta(1L);
        });
    }
}
