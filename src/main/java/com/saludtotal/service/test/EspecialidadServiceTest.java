package com.saludtotal.service.test;

import com.saludtotal.clinica.models.Especialidad;
import com.saludtotal.clinica.models.Estado;
import com.saludtotal.dto.EspecialidadDTO;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.repositories.EspecialidadRepository;
import com.saludtotal.repositories.EstadoRepository;

import com.saludtotal.service.EspecialidadService;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EspecialidadServiceTest {

    @Mock
    private EspecialidadRepository especialidadRepository;

    @Mock
    private EstadoRepository estadoRepository;

    @InjectMocks
    private EspecialidadService especialidadService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testListarTodos() {
        Especialidad especialidad = crearEspecialidad();
        when(especialidadRepository.findAll()).thenReturn(List.of(especialidad));

        List<EspecialidadDTO> resultado = especialidadService.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Cardiología", resultado.get(0).getNombre());
        verify(especialidadRepository).findAll();
    }

    @Test
    void testBuscarPorId_Existe() {
        Especialidad especialidad = crearEspecialidad();
        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(especialidad));

        EspecialidadDTO dto = especialidadService.buscarPorId(1L);

        assertEquals(1L, dto.getIdEspecialidad());
        assertEquals("Cardiología", dto.getNombre());
    }

    @Test
    void testBuscarPorId_NoExiste() {
        when(especialidadRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> especialidadService.buscarPorId(999L));
    }

    @Test
    void testRegistrarEspecialidad() {
        EspecialidadDTO dto = crearEspecialidadDTO();
        Especialidad guardada = crearEspecialidad();

        when(especialidadRepository.save(any(Especialidad.class))).thenReturn(guardada);

        EspecialidadDTO resultado = especialidadService.registrarEspecialidad(dto);

        assertNotNull(resultado);
        assertEquals("Cardiología", resultado.getNombre());
        verify(especialidadRepository).save(any(Especialidad.class));
    }

    @Test
    void testActualizarEspecialidad_Existe() {
        Especialidad existente = crearEspecialidad();
        EspecialidadDTO dto = crearEspecialidadDTO();
        dto.setDescripcion("Nueva descripción");
        dto.setIdEstado(30L);

        Estado nuevoEstado = new Estado();
        nuevoEstado.setIdEstado(30L);

        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(estadoRepository.findById(30L)).thenReturn(Optional.of(nuevoEstado));
        when(especialidadRepository.save(any(Especialidad.class))).thenAnswer(i -> i.getArgument(0));

        EspecialidadDTO resultado = especialidadService.actualizarEspecialidad(1L, dto);

        assertEquals("Nueva descripción", resultado.getDescripcion());
        assertEquals(30L, resultado.getIdEstado());
    }

    @Test
    void testActualizarEspecialidad_NoExiste() {
        when(especialidadRepository.findById(999L)).thenReturn(Optional.empty());
        EspecialidadDTO dto = crearEspecialidadDTO();

        assertThrows(RecursoNoEncontradoException.class, () -> especialidadService.actualizarEspecialidad(999L, dto));
    }

    @Test
    void testActualizarEspecialidad_EstadoNoExiste() {
        Especialidad existente = crearEspecialidad();
        EspecialidadDTO dto = crearEspecialidadDTO();
        dto.setIdEstado(404L);

        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(estadoRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> especialidadService.actualizarEspecialidad(1L, dto));
    }

    @Test
    void testEliminarEspecialidad_Existe() {
        Especialidad especialidad = crearEspecialidad();
        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(especialidad));

        especialidadService.eliminarEspecialidad(1L);

        verify(especialidadRepository).delete(especialidad);
    }

    @Test
    void testEliminarEspecialidad_NoExiste() {
        when(especialidadRepository.findById(888L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> especialidadService.eliminarEspecialidad(888L));
    }

    // ---------- Métodos auxiliares ----------

    private Especialidad crearEspecialidad() {
        Especialidad especialidad = new Especialidad();
        especialidad.setIdEspecialidad(1L);
        especialidad.setNombre("Cardiología");
        especialidad.setDescripcion("Estudio del corazón");

        Estado estado = new Estado(10L, "activo");  // id=10, nombre=activo
        especialidad.setEstado(estado);

        return especialidad;
    }


    private EspecialidadDTO crearEspecialidadDTO() {
        EspecialidadDTO dto = new EspecialidadDTO();
        dto.setIdEspecialidad(1L);
        dto.setNombre("Cardiología");
        dto.setDescripcion("Estudio del corazón");
        dto.setIdEstado(10L);
        return dto;
    }
}
