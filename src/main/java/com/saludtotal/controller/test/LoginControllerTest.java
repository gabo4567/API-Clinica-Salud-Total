package com.saludtotal.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saludtotal.controller.LoginController;
import com.saludtotal.dto.LoginRequestDTO;
import com.saludtotal.dto.LoginResponseDTO;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.service.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @Autowired
    private ObjectMapper objectMapper;

    private LoginRequestDTO buildRequest(String email, String contrasenia) {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail(email);
        request.setContrasenia(contrasenia);
        return request;
    }

    @Test
    void loginPaciente_Success() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("paciente@correo.com");
        request.setContrasenia("1234");

        LoginResponseDTO response = new LoginResponseDTO("Login exitoso");

        when(loginService.loginPaciente(request.getEmail(), request.getContrasenia()))
                .thenReturn(response);

        mockMvc.perform(post("/api/login/paciente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login exitoso"));

        verify(loginService, times(1)).loginPaciente(request.getEmail(), request.getContrasenia());
    }

    @Test
    void loginPaciente_Failure_UserNotFound() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("noexiste@correo.com");
        request.setContrasenia("1234");

        when(loginService.loginPaciente(request.getEmail(), request.getContrasenia()))
                .thenThrow(new RecursoNoEncontradoException("Usuario no encontrado"));

        mockMvc.perform(post("/api/login/paciente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuario no encontrado"));

        verify(loginService, times(1)).loginPaciente(request.getEmail(), request.getContrasenia());
    }

    @Test
    void loginProfesional_Success() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("profesional@correo.com");
        request.setContrasenia("abcd");

        LoginResponseDTO response = new LoginResponseDTO("Login exitoso");

        when(loginService.loginProfesional(request.getEmail(), request.getContrasenia()))
                .thenReturn(response);

        mockMvc.perform(post("/api/login/profesional")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login exitoso"));

        verify(loginService, times(1)).loginProfesional(request.getEmail(), request.getContrasenia());
    }

    @Test
    void loginProfesional_Failure_PasswordIncorrect() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("profesional@correo.com");
        request.setContrasenia("wrongpass");

        when(loginService.loginProfesional(request.getEmail(), request.getContrasenia()))
                .thenThrow(new RecursoNoEncontradoException("Contraseña incorrecta"));

        mockMvc.perform(post("/api/login/profesional")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Contraseña incorrecta"));

        verify(loginService, times(1)).loginProfesional(request.getEmail(), request.getContrasenia());
    }

    @Test
    void loginSecretaria_Success() throws Exception {
        LoginRequestDTO request = buildRequest("secretaria@correo.com", "clave123");

        when(loginService.loginSecretaria(request.getEmail(), request.getContrasenia()))
                .thenReturn(new LoginResponseDTO("Login de secretaria exitoso"));

        mockMvc.perform(post("/api/login/secretaria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login de secretaria exitoso"));

        verify(loginService, times(1)).loginSecretaria(request.getEmail(), request.getContrasenia());
    }

    @Test
    void loginSecretaria_Failure_UserNotFound() throws Exception {
        LoginRequestDTO request = buildRequest("noexiste@correo.com", "clave123");

        when(loginService.loginSecretaria(request.getEmail(), request.getContrasenia()))
                .thenThrow(new RecursoNoEncontradoException("Usuario no encontrado"));

        mockMvc.perform(post("/api/login/secretaria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuario no encontrado"));

        verify(loginService, times(1)).loginSecretaria(request.getEmail(), request.getContrasenia());
    }

    @Test
    void loginSecretaria_Failure_WrongPassword() throws Exception {
        LoginRequestDTO request = buildRequest("secretaria@correo.com", "wrongpass");

        when(loginService.loginSecretaria(request.getEmail(), request.getContrasenia()))
                .thenThrow(new RecursoNoEncontradoException("Contraseña incorrecta"));

        mockMvc.perform(post("/api/login/secretaria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Contraseña incorrecta"));

        verify(loginService, times(1)).loginSecretaria(request.getEmail(), request.getContrasenia());
    }
}
