package com.saludtotal.service.test;

import com.saludtotal.clinica.models.Persona;
import com.saludtotal.clinica.models.Paciente;
import com.saludtotal.clinica.models.Profesional;
import com.saludtotal.dto.LoginResponseDTO;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.repositories.PacienteRepository;
import com.saludtotal.repositories.ProfesionalRepository;
import com.saludtotal.repositories.SecretariaRepository;
import com.saludtotal.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private ProfesionalRepository profesionalRepository;

    @Mock
    private SecretariaRepository secretariaRepository;

    @InjectMocks
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- Tests para loginPaciente ---

    @Test
    void loginPaciente_Success() {
        String email = "paciente@correo.com";
        String password = "1234";

        Persona persona = new Persona();
        persona.setContrasenia(password);

        Paciente paciente = new Paciente();
        paciente.setPersona(persona);

        when(pacienteRepository.findByPersonaEmail(email)).thenReturn(Optional.of(paciente));

        LoginResponseDTO response = loginService.loginPaciente(email, password);

        assertEquals("Login de paciente exitoso", response.getMessage());
    }

    @Test
    void loginPaciente_UsuarioNoEncontrado() {
        String email = "noexiste@correo.com";
        String password = "1234";

        when(pacienteRepository.findByPersonaEmail(email)).thenReturn(Optional.empty());

        RecursoNoEncontradoException ex = assertThrows(RecursoNoEncontradoException.class, () ->
                loginService.loginPaciente(email, password)
        );
        assertEquals("Paciente no encontrado", ex.getMessage());
    }

    @Test
    void loginPaciente_ContraseniaIncorrecta() {
        String email = "paciente@correo.com";
        String password = "1234";
        String passIncorrecto = "4321";

        Persona persona = new Persona();
        persona.setContrasenia(password);

        Paciente paciente = new Paciente();
        paciente.setPersona(persona);

        when(pacienteRepository.findByPersonaEmail(email)).thenReturn(Optional.of(paciente));

        RecursoNoEncontradoException ex = assertThrows(RecursoNoEncontradoException.class, () ->
                loginService.loginPaciente(email, passIncorrecto)
        );
        assertEquals("Contraseña incorrecta", ex.getMessage());
    }

    // --- Tests para loginProfesional ---

    @Test
    void loginProfesional_Success() {
        String email = "profesional@correo.com";
        String password = "abcd";

        Persona persona = new Persona();
        persona.setContrasenia(password);

        Profesional profesional = new Profesional();
        profesional.setPersona(persona);

        when(profesionalRepository.findByEmail(email)).thenReturn(Optional.of(profesional));

        LoginResponseDTO response = loginService.loginProfesional(email, password);

        assertEquals("Login de profesional exitoso", response.getMessage());
    }

    @Test
    void loginProfesional_UsuarioNoEncontrado() {
        String email = "noexiste@correo.com";
        String password = "abcd";

        when(profesionalRepository.findByEmail(email)).thenReturn(Optional.empty());

        RecursoNoEncontradoException ex = assertThrows(RecursoNoEncontradoException.class, () ->
                loginService.loginProfesional(email, password)
        );
        assertEquals("Profesional no encontrado", ex.getMessage());
    }

    @Test
    void loginProfesional_ContraseniaIncorrecta() {
        String email = "profesional@correo.com";
        String password = "abcd";
        String passIncorrecto = "dcba";

        Persona persona = new Persona();
        persona.setContrasenia(password);

        Profesional profesional = new Profesional();
        profesional.setPersona(persona);

        when(profesionalRepository.findByEmail(email)).thenReturn(Optional.of(profesional));

        RecursoNoEncontradoException ex = assertThrows(RecursoNoEncontradoException.class, () ->
                loginService.loginProfesional(email, passIncorrecto)
        );
        assertEquals("Contraseña incorrecta", ex.getMessage());
    }

    @Test
    void loginSecretaria_Success() {
        String email = "secretaria@correo.com";
        String password = "clave123";

        Persona secretaria = new Persona();
        secretaria.setEmail(email);
        secretaria.setContrasenia(password);

        when(secretariaRepository.findByEmail(email)).thenReturn(Optional.of(secretaria));

        LoginResponseDTO response = loginService.loginSecretaria(email, password);

        assertEquals("Login de secretaria exitoso", response.getMessage());
    }

    @Test
    void loginSecretaria_UserNotFound() {
        String email = "noexiste@correo.com";
        String password = "clave123";

        when(secretariaRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () ->
                loginService.loginSecretaria(email, password));
    }

    @Test
    void loginSecretaria_IncorrectPassword() {
        String email = "secretaria@correo.com";
        String passwordCorrecta = "clave123";
        String passwordIncorrecta = "otraClave";

        Persona secretaria = new Persona();
        secretaria.setEmail(email);
        secretaria.setContrasenia(passwordCorrecta);

        when(secretariaRepository.findByEmail(email)).thenReturn(Optional.of(secretaria));

        assertThrows(RecursoNoEncontradoException.class, () ->
                loginService.loginSecretaria(email, passwordIncorrecta));
    }

}
