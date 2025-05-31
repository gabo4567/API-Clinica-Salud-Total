package com.saludtotal.controller.test;

import com.saludtotal.controller.PacienteController;
import com.saludtotal.exceptions.EntidadYaExisteException;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.service.PacienteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PacienteController.class)
class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteService pacienteService;

    // --- Tests existentes para errores y casos sin contenido ---

    @Test
    void obtenerPacientePorId_Inexistente_DeberiaRetornar404() throws Exception {
        Long idInexistente = 999L;
        String mensaje = "Paciente no encontrado con ID: " + idInexistente;

        Mockito.when(pacienteService.buscarPorId(idInexistente))
                .thenThrow(new RecursoNoEncontradoException(mensaje));

        mockMvc.perform(get("/api/pacientes/" + idInexistente))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(mensaje));
    }

    @Test
    void obtenerPacientePorDni_Inexistente_DeberiaRetornar404() throws Exception {
        String dni = "12345678";
        String mensaje = "Paciente no encontrado con DNI: " + dni;

        Mockito.when(pacienteService.buscarPorDni(dni))
                .thenThrow(new RecursoNoEncontradoException(mensaje));

        mockMvc.perform(get("/api/pacientes/dni/" + dni))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(mensaje));
    }

    @Test
    void registrarPaciente_DniDuplicado_DeberiaRetornar409() throws Exception {
        String mensaje = "Ya existe una persona con el DNI 12345678";

        Mockito.when(pacienteService.registrarPaciente(any()))
                .thenThrow(new EntidadYaExisteException(mensaje));

        String json = """
        {
            "dni": "12345678",
            "nombre": "Juan",
            "apellido": "Pérez",
            "email": "juan@mail.com",
            "telefono": "123456789",
            "direccion": "Calle Falsa 123",
            "idRol": 1,
            "idEspecialidad": 1,
            "idEstadoPersona": 1,
            "fechaNacimiento": "1990-01-01",
            "obraSocial": "OSDE",
            "idEstadoPaciente": 1
        }
    """;

        mockMvc.perform(post("/api/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(mensaje)); // Este sí es plano, porque tu handler devuelve solo el string
    }

    @Test
    void actualizarPaciente_Inexistente_DeberiaRetornar404() throws Exception {
        Long id = 999L;
        String mensaje = "Paciente no encontrado con ID: " + id;

        Mockito.when(pacienteService.actualizarPaciente(Mockito.eq(id), any()))
                .thenThrow(new RecursoNoEncontradoException(mensaje));

        String json = """
        {
            "dni": "12345678",
            "nombre": "Juan",
            "apellido": "Pérez",
            "email": "juan@mail.com",
            "telefono": "123456789",
            "direccion": "Calle Falsa 123",
            "idRol": 1,
            "idEspecialidad": 1,
            "idEstadoPersona": 1,
            "fechaNacimiento": "1990-01-01",
            "obraSocial": "OSDE",
            "idEstadoPaciente": 1
        }
    """;

        mockMvc.perform(put("/api/pacientes/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(mensaje));
    }

    @Test
    void eliminarPaciente_Inexistente_DeberiaRetornar404() throws Exception {
        Long id = 123L;
        String mensaje = "Paciente no encontrado con ID: " + id;

        doThrow(new RecursoNoEncontradoException(mensaje))
                .when(pacienteService).eliminarPaciente(id);

        mockMvc.perform(delete("/api/pacientes/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(mensaje));
    }

    @Test
    void obtenerPorNombre_SinResultados_DeberiaRetornar204() throws Exception {
        Mockito.when(pacienteService.buscarPorNombre("inexistente"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/pacientes/nombre/inexistente"))
                .andExpect(status().isNoContent());
    }

    @Test
    void obtenerPorApellido_SinResultados_DeberiaRetornar204() throws Exception {
        Mockito.when(pacienteService.buscarPorApellido("desconocido"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/pacientes/apellido/desconocido"))
                .andExpect(status().isNoContent());
    }

    // --- Tests para casos exitosos ---

    @Test
    void obtenerPacientePorId_Existente_DeberiaRetornar200ConPaciente() throws Exception {
        Long id = 1L;
        String dni = "12345678";
        String nombre = "Juan";

        String pacienteJson = """
            {
                "id": %d,
                "persona": {
                    "dni": "%s",
                    "nombre": "%s"
                },
                "obraSocial": "OSDE"
            }
            """.formatted(id, dni, nombre);

        var paciente = new com.saludtotal.clinica.models.Paciente();
        var persona = new com.saludtotal.clinica.models.Persona();
        persona.setDni(dni);
        persona.setNombre(nombre);
        paciente.setId(id);
        paciente.setPersona(persona);
        paciente.setObraSocial("OSDE");

        when(pacienteService.buscarPorId(id)).thenReturn(paciente);

        mockMvc.perform(get("/api/pacientes/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(pacienteJson));
    }

    @Test
    void obtenerPacientePorDni_Existente_DeberiaRetornar200ConPaciente() throws Exception {
        String dni = "12345678";
        String nombre = "Juan";

        String pacienteJson = """
            {
                "persona": {
                    "dni": "%s",
                    "nombre": "%s"
                },
                "obraSocial": "OSDE"
            }
            """.formatted(dni, nombre);

        var paciente = new com.saludtotal.clinica.models.Paciente();
        var persona = new com.saludtotal.clinica.models.Persona();
        persona.setDni(dni);
        persona.setNombre(nombre);
        paciente.setPersona(persona);
        paciente.setObraSocial("OSDE");

        when(pacienteService.buscarPorDni(dni)).thenReturn(paciente);

        mockMvc.perform(get("/api/pacientes/dni/" + dni))
                .andExpect(status().isOk())
                .andExpect(content().json(pacienteJson));
    }

    @Test
    void registrarPaciente_Exitoso_DeberiaRetornar201() throws Exception {
        String jsonRequest = """
            {
                "dni": "12345678",
                "nombre": "Juan",
                "apellido": "Pérez",
                "email": "juan@mail.com",
                "telefono": "123456789",
                "direccion": "Calle Falsa 123",
                "idRol": 1,
                "idEspecialidad": 1,
                "idEstadoPersona": 1,
                "fechaNacimiento": "1990-01-01",
                "obraSocial": "OSDE",
                "idEstadoPaciente": 1
            }
        """;

        var paciente = new com.saludtotal.clinica.models.Paciente();
        var persona = new com.saludtotal.clinica.models.Persona();
        persona.setDni("12345678");
        persona.setNombre("Juan");
        paciente.setPersona(persona);
        paciente.setObraSocial("OSDE");

        when(pacienteService.registrarPaciente(any())).thenReturn(paciente);

        mockMvc.perform(post("/api/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                    {
                        "persona": {
                            "dni": "12345678",
                            "nombre": "Juan"
                        },
                        "obraSocial": "OSDE"
                    }
                """));
    }

    @Test
    void actualizarPaciente_Existente_DeberiaRetornar200ConPacienteActualizado() throws Exception {
        Long id = 1L;

        String jsonRequest = """
            {
                "dni": "12345678",
                "nombre": "Juan",
                "apellido": "Pérez",
                "email": "juan@mail.com",
                "telefono": "123456789",
                "direccion": "Calle Falsa 123",
                "idRol": 1,
                "idEspecialidad": 1,
                "idEstadoPersona": 1,
                "fechaNacimiento": "1990-01-01",
                "obraSocial": "OSDE",
                "idEstadoPaciente": 1
            }
        """;

        var paciente = new com.saludtotal.clinica.models.Paciente();
        var persona = new com.saludtotal.clinica.models.Persona();
        persona.setDni("12345678");
        persona.setNombre("Juan");
        paciente.setPersona(persona);
        paciente.setObraSocial("OSDE");

        when(pacienteService.actualizarPaciente(Mockito.eq(id), any())).thenReturn(paciente);

        mockMvc.perform(put("/api/pacientes/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    {
                        "persona": {
                            "dni": "12345678",
                            "nombre": "Juan"
                        },
                        "obraSocial": "OSDE"
                    }
                """));
    }

    @Test
    void eliminarPaciente_Existente_DeberiaRetornar204() throws Exception {
        Long id = 1L;

        // No lanzamos excepciones, la eliminación es exitosa
        mockMvc.perform(delete("/api/pacientes/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void obtenerPorNombre_ConResultados_DeberiaRetornar200ConLista() throws Exception {
        String nombre = "Juan";

        var paciente = new com.saludtotal.clinica.models.Paciente();
        var persona = new com.saludtotal.clinica.models.Persona();
        persona.setNombre(nombre);
        paciente.setPersona(persona);
        paciente.setObraSocial("OSDE");

        when(pacienteService.buscarPorNombre(nombre)).thenReturn(List.of(paciente));

        mockMvc.perform(get("/api/pacientes/nombre/" + nombre))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].persona.nombre").value(nombre))
                .andExpect(jsonPath("$[0].obraSocial").value("OSDE"));
    }

    @Test
    void obtenerPorApellido_ConResultados_DeberiaRetornar200ConLista() throws Exception {
        String apellido = "Pérez";

        var paciente = new com.saludtotal.clinica.models.Paciente();
        var persona = new com.saludtotal.clinica.models.Persona();
        persona.setApellido(apellido);
        paciente.setPersona(persona);
        paciente.setObraSocial("OSDE");

        when(pacienteService.buscarPorApellido(apellido)).thenReturn(List.of(paciente));

        mockMvc.perform(get("/api/pacientes/apellido/" + apellido))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].persona.apellido").value(apellido))
                .andExpect(jsonPath("$[0].obraSocial").value("OSDE"));
    }
}
