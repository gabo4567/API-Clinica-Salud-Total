package com.saludtotal.service;

import com.saludtotal.clinica.models.Paciente;
import com.saludtotal.clinica.models.Persona;
import com.saludtotal.dto.RegistroPacienteDTO;
import com.saludtotal.exceptions.EntidadYaExisteException;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.repositories.PacienteRepository;
import com.saludtotal.repositories.PersonaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PersonaRepository personaRepository;

    public PacienteService(PacienteRepository pacienteRepository, PersonaRepository personaRepository) {
        this.pacienteRepository = pacienteRepository;
        this.personaRepository = personaRepository;
    }

    public Paciente registrarPaciente(RegistroPacienteDTO dto) {
        if (personaRepository.existsByDni(dto.getDni())) {
            throw new EntidadYaExisteException("Ya existe una persona con el DNI " + dto.getDni());
        }

        if (pacienteRepository.existsByPersonaDni(dto.getDni())) {
            throw new EntidadYaExisteException("Ya existe un paciente registrado con el DNI " + dto.getDni());
        }

        Persona persona = new Persona();
        persona.setDni(dto.getDni());
        persona.setNombre(dto.getNombre());
        persona.setApellido(dto.getApellido());
        persona.setEmail(dto.getEmail());
        persona.setTelefono(dto.getTelefono());
        persona.setDireccion(dto.getDireccion());
        persona.setIdRol(dto.getIdRol());
        persona.setIdEspecialidad(dto.getIdEspecialidad());
        persona.setIdEstado(dto.getIdEstadoPersona());
        persona.setFechaNacimiento(dto.getFechaNacimiento());

        // ✅ Guardamos la contraseña en Persona
        persona.setContrasenia(dto.getContrasenia());

        Persona personaGuardada = personaRepository.save(persona);

        Paciente paciente = new Paciente();
        paciente.setPersona(personaGuardada);
        paciente.setObraSocial(dto.getObraSocial());
        paciente.setIdEstado(dto.getIdEstadoPaciente());

        return pacienteRepository.save(paciente);
    }

    public Paciente actualizarPaciente(Long idPaciente, RegistroPacienteDTO dto) {
        Paciente pacienteExistente = buscarPorId(idPaciente);

        Persona persona = pacienteExistente.getPersona();
        persona.setDni(dto.getDni());
        persona.setNombre(dto.getNombre());
        persona.setApellido(dto.getApellido());
        persona.setEmail(dto.getEmail());
        persona.setTelefono(dto.getTelefono());
        persona.setDireccion(dto.getDireccion());
        persona.setIdRol(dto.getIdRol());
        persona.setIdEspecialidad(dto.getIdEspecialidad());
        persona.setIdEstado(dto.getIdEstadoPersona());
        persona.setFechaNacimiento(dto.getFechaNacimiento());

        // Actualizar la contraseña también
        persona.setContrasenia(dto.getContrasenia());

        Persona personaActualizada = personaRepository.save(persona);

        pacienteExistente.setPersona(personaActualizada);
        pacienteExistente.setObraSocial(dto.getObraSocial());
        pacienteExistente.setIdEstado(dto.getIdEstadoPaciente());

        return pacienteRepository.save(pacienteExistente);
    }

    public Paciente buscarPorDni(String dni) {
        return pacienteRepository.findByPersonaDni(dni)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado con DNI: " + dni));
    }

    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado con ID: " + id));
    }

    public void eliminarPaciente(Long id) {
        Paciente paciente = buscarPorId(id);
        pacienteRepository.delete(paciente);
    }

    public List<Paciente> obtenerTodos() {
        return pacienteRepository.findAll();
    }

    public List<Paciente> buscarPorNombre(String nombre) {
        return pacienteRepository.findByPersonaNombreContainingIgnoreCase(nombre);
    }

    public List<Paciente> buscarPorApellido(String apellido) {
        return pacienteRepository.findByPersonaApellidoContainingIgnoreCase(apellido);
    }

    public Optional<Paciente> login(String email, String contrasenia) {
        return pacienteRepository.findByPersonaEmail(email)
                .filter(p -> p.getPersona().getContrasenia().equals(contrasenia));
    }


}
