package com.saludtotal.service;

import com.saludtotal.clinica.models.Paciente;
import com.saludtotal.clinica.models.Persona;
import com.saludtotal.dto.RegistroPacienteDTO;
import com.saludtotal.exceptions.EntidadYaExisteException;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.repositories.PacienteRepository;
import com.saludtotal.repositories.PersonaRepository;
import jakarta.transaction.Transactional;
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

        Persona personaGuardada = personaRepository.save(persona);

        Paciente paciente = new Paciente();
        paciente.setPersona(personaGuardada);
        paciente.setObraSocial(dto.getObraSocial());
        paciente.setIdEstado(dto.getIdEstadoPaciente());

        return pacienteRepository.save(paciente);
    }

    public Paciente actualizarPaciente(Long idPaciente, RegistroPacienteDTO dto) {
        System.out.println("---- DATOS RECIBIDOS PARA ACTUALIZAR ----");
        System.out.println("ID paciente: " + idPaciente);
        System.out.println("DTO: " + dto);

        // Resto del código
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

        Persona personaActualizada = personaRepository.save(persona);

        pacienteExistente.setPersona(personaActualizada);
        pacienteExistente.setObraSocial(dto.getObraSocial());
        pacienteExistente.setIdEstado(dto.getIdEstadoPaciente());

        System.out.println("ID Rol: " + dto.getIdRol());
        System.out.println("ID Especialidad: " + dto.getIdEspecialidad());
        System.out.println("ID Estado Persona: " + dto.getIdEstadoPersona());
        System.out.println("Fecha nacimiento: " + dto.getFechaNacimiento());

        return pacienteRepository.save(pacienteExistente);
    }


    public Paciente buscarPorDni(String dni) {
        return pacienteRepository.findByPersonaDni(dni)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado con DNI: " + dni));
    }

    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findByIdConPersona(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado con ID: " + id));
    }

    @Transactional
    public void eliminarPaciente(Long idPaciente) {
        System.out.println(">>> Llega al backend solicitud para eliminar paciente con ID: " + idPaciente);

        Paciente paciente = buscarPorId(idPaciente);
        System.out.println(">>> Paciente encontrado: ID=" + paciente.getId() + ", Estado actual=" + paciente.getIdEstado());

        Persona persona = paciente.getPersona();
        if (persona != null) {
            // Buscar la persona "attachada" desde la base para evitar problemas con detached entities
            Persona personaAttached = personaRepository.findById(persona.getId()).orElse(null);
            if (personaAttached != null) {
                System.out.println(">>> Persona asociada encontrada (attached): ID=" + personaAttached.getId() + ", Estado actual=" + personaAttached.getIdEstado());
                personaAttached.setIdEstado(2L);
                System.out.println(">>> Estado Persona después: " + personaAttached.getIdEstado());
                personaRepository.saveAndFlush(personaAttached);
            } else {
                System.out.println(">>> No se encontró persona attachada con ID: " + persona.getId());
            }
        } else {
            System.out.println(">>> La persona asociada al paciente es NULL");
        }

        paciente.setIdEstado(2L);
        pacienteRepository.saveAndFlush(paciente);
        System.out.println(">>> Paciente con ID " + paciente.getId() + " marcado como inactivo.");
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


}
