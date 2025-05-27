package com.saludtotal.service.test;

import com.saludtotal.clinica.models.Paciente;
import com.saludtotal.clinica.models.Persona;
import com.saludtotal.dto.RegistroPacienteDTO;
import com.saludtotal.exceptions.EntidadYaExisteException;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.repositories.PacienteRepository;
import com.saludtotal.repositories.PersonaRepository;
import com.saludtotal.service.PacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private PacienteService pacienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registrarPaciente_DeberiaGuardarYRetornarPaciente() {
        RegistroPacienteDTO dto = crearDto();
        when(personaRepository.existsByDni(dto.getDni())).thenReturn(false);
        when(pacienteRepository.existsByPersonaDni(dto.getDni())).thenReturn(false);

        Persona personaGuardada = new Persona();
        personaGuardada.setDni(dto.getDni());
        when(personaRepository.save(any(Persona.class))).thenReturn(personaGuardada);

        Paciente pacienteGuardado = new Paciente();
        pacienteGuardado.setPersona(personaGuardada);
        pacienteGuardado.setObraSocial(dto.getObraSocial());
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteGuardado);

        Paciente resultado = pacienteService.registrarPaciente(dto);

        assertNotNull(resultado);
        assertEquals(dto.getDni(), resultado.getPersona().getDni());
        assertEquals(dto.getObraSocial(), resultado.getObraSocial());
    }

    @Test
    void buscarPorDni_PacienteExistente_DeberiaRetornarPaciente() {
        Paciente paciente = new Paciente();
        paciente.setPersona(new Persona());
        paciente.getPersona().setDni("12345678");

        when(pacienteRepository.findByPersonaDni("12345678")).thenReturn(Optional.of(paciente));

        Paciente resultado = pacienteService.buscarPorDni("12345678");

        assertNotNull(resultado);
        assertEquals("12345678", resultado.getPersona().getDni());
    }

    @Test
    void buscarPorDni_PacienteInexistente_DeberiaLanzarExcepcion() {
        when(pacienteRepository.findByPersonaDni("00000000")).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> pacienteService.buscarPorDni("00000000"));
    }

    @Test
    void buscarPorId_PacienteExistente_DeberiaRetornarPaciente() {
        Paciente paciente = new Paciente();
        paciente.setId(1L);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        Paciente resultado = pacienteService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void buscarPorId_PacienteInexistente_DeberiaLanzarExcepcion() {
        when(pacienteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> pacienteService.buscarPorId(999L));
    }

    @Test
    void registrarPaciente_DniYaRegistradoComoPersona_DeberiaLanzarExcepcion() {
        RegistroPacienteDTO dto = crearDto();
        when(personaRepository.existsByDni(dto.getDni())).thenReturn(true);

        assertThrows(EntidadYaExisteException.class, () -> pacienteService.registrarPaciente(dto));
    }

    @Test
    void registrarPaciente_DniYaRegistradoComoPaciente_DeberiaLanzarExcepcion() {
        RegistroPacienteDTO dto = crearDto();
        when(personaRepository.existsByDni(dto.getDni())).thenReturn(false);
        when(pacienteRepository.existsByPersonaDni(dto.getDni())).thenReturn(true);

        assertThrows(EntidadYaExisteException.class, () -> pacienteService.registrarPaciente(dto));
    }

    private RegistroPacienteDTO crearDto() {
        RegistroPacienteDTO dto = new RegistroPacienteDTO();
        dto.setDni("12345678");
        dto.setNombre("Juan");
        dto.setApellido("Pérez");
        dto.setEmail("juan@example.com");
        dto.setTelefono("1234567890");
        dto.setDireccion("Calle Falsa 123");
        dto.setIdRol(1L);
        dto.setIdEspecialidad(2L);
        dto.setIdEstadoPersona(1L);
        dto.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        dto.setObraSocial("Obra Social X");
        dto.setIdEstadoPaciente(1L);
        return dto;
    }
}
