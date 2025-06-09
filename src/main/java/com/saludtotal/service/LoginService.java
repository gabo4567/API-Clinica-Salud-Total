package com.saludtotal.service;

import com.saludtotal.clinica.models.Persona;
import com.saludtotal.dto.LoginResponseDTO;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.clinica.models.Paciente;
import com.saludtotal.clinica.models.Profesional;
import com.saludtotal.repositories.PacienteRepository;
import com.saludtotal.repositories.ProfesionalRepository;
import com.saludtotal.repositories.SecretariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ProfesionalRepository profesionalRepository;

    @Autowired
    private SecretariaRepository secretariaRepository;

    public LoginResponseDTO loginPaciente(String email, String password) {
        Optional<Paciente> pacienteOpt = pacienteRepository.findByPersonaEmail(email);

        if (pacienteOpt.isEmpty()) {
            throw new RecursoNoEncontradoException("Paciente no encontrado");
        }

        Paciente paciente = pacienteOpt.get();

        if (!paciente.getPersona().getContrasenia().equals(password)) {
            throw new RecursoNoEncontradoException("Contraseña incorrecta");
        }

        return new LoginResponseDTO("Login de paciente exitoso");
    }

    public LoginResponseDTO loginProfesional(String email, String password) {
        Optional<Profesional> profesionalOpt = profesionalRepository.findByEmail(email);

        if (profesionalOpt.isEmpty()) {
            throw new RecursoNoEncontradoException("Profesional no encontrado");
        }

        Profesional profesional = profesionalOpt.get();

        if (!profesional.getPersona().getContrasenia().equals(password)) {
            throw new RecursoNoEncontradoException("Contraseña incorrecta");
        }

        return new LoginResponseDTO("Login de profesional exitoso");
    }

    public LoginResponseDTO loginSecretaria(String email, String password) {
        Optional<Persona> secretariaOpt = secretariaRepository.findByEmail(email);

        if (secretariaOpt.isEmpty()) {
            throw new RecursoNoEncontradoException("Usuario no encontrado");
        }

        Persona secretaria = secretariaOpt.get();

        if (!secretaria.getContrasenia().equals(password)) {
            throw new RecursoNoEncontradoException("Contraseña incorrecta");
        }

        return new LoginResponseDTO("Login de secretaria exitoso");
    }
}
