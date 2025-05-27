package com.saludtotal.service.test;

import com.saludtotal.clinica.models.Especialidad;
import com.saludtotal.clinica.models.Estado;
import com.saludtotal.clinica.models.Persona;
import com.saludtotal.clinica.models.Profesional;
import com.saludtotal.dto.ProfesionalDTO;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.repositories.ProfesionalRepository;

import com.saludtotal.service.ProfesionalService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfesionalServiceTest {

    @Mock
    private ProfesionalRepository profesionalRepository;

    @InjectMocks
    private ProfesionalService profesionalService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarTodos() {
        Profesional profesional = crearProfesional();
        when(profesionalRepository.findAll()).thenReturn(List.of(profesional));

        List<ProfesionalDTO> resultado = profesionalService.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals(profesional.getIdProfesional(), resultado.get(0).getId());
        verify(profesionalRepository).findAll();
    }

    @Test
    void testBuscarPorId_Existe() {
        Profesional profesional = crearProfesional();
        when(profesionalRepository.findById(1L)).thenReturn(Optional.of(profesional));

        ProfesionalDTO dto = profesionalService.buscarPorId(1L);

        assertEquals(1L, dto.getId());
        assertEquals("MAT-123", dto.getMatriculaProfesional());
    }

    @Test
    void testBuscarPorId_NoExiste() {
        when(profesionalRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> profesionalService.buscarPorId(99L));
    }

    @Test
    void testRegistrarProfesional() {
        ProfesionalDTO dto = crearProfesionalDTO();
        Profesional profesionalGuardado = crearProfesional();

        when(profesionalRepository.save(any(Profesional.class))).thenReturn(profesionalGuardado);

        ProfesionalDTO resultado = profesionalService.registrarProfesional(dto);

        assertNotNull(resultado);
        assertEquals("MAT-123", resultado.getMatriculaProfesional());
        verify(profesionalRepository).save(any(Profesional.class));
    }

    @Test
    void testActualizarProfesional_Existe() {
        Profesional existente = crearProfesional();
        Profesional actualizado = crearProfesional();
        actualizado.setMatriculaProfesional("MAT-456");

        ProfesionalDTO dto = crearProfesionalDTO();
        dto.setMatriculaProfesional("MAT-456");

        when(profesionalRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(profesionalRepository.save(any(Profesional.class))).thenReturn(actualizado);

        ProfesionalDTO resultado = profesionalService.actualizarProfesional(1L, dto);

        assertEquals("MAT-456", resultado.getMatriculaProfesional());
    }

    @Test
    void testActualizarProfesional_NoExiste() {
        when(profesionalRepository.findById(999L)).thenReturn(Optional.empty());
        ProfesionalDTO dto = crearProfesionalDTO();

        assertThrows(RecursoNoEncontradoException.class, () -> profesionalService.actualizarProfesional(999L, dto));
    }

    @Test
    void testEliminarProfesional_Existe() {
        Profesional profesional = crearProfesional();
        when(profesionalRepository.findById(1L)).thenReturn(Optional.of(profesional));

        profesionalService.eliminarProfesional(1L);

        verify(profesionalRepository).delete(profesional);
    }

    @Test
    void testEliminarProfesional_NoExiste() {
        when(profesionalRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> profesionalService.eliminarProfesional(123L));
    }

    // --------- Métodos auxiliares ---------

    private Profesional crearProfesional() {
        Profesional profesional = new Profesional();
        profesional.setIdProfesional(1L);
        profesional.setMatriculaProfesional("MAT-123");

        Persona persona = new Persona();
        persona.setId(10L);
        profesional.setPersona(persona);

        Especialidad especialidad = new Especialidad();
        especialidad.setIdEspecialidad(20L);
        profesional.setEspecialidad(especialidad);

        Estado estado = new Estado();
        estado.setIdEstado(30L);
        profesional.setEstado(estado);

        return profesional;
    }

    private ProfesionalDTO crearProfesionalDTO() {
        ProfesionalDTO dto = new ProfesionalDTO();
        dto.setId(1L);
        dto.setIdPersona(10L);
        dto.setIdEspecialidad(20L);
        dto.setMatriculaProfesional("MAT-123");
        dto.setIdEstado(30L);
        return dto;
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
