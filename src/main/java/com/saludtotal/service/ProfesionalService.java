package com.saludtotal.service;

import com.saludtotal.clinica.models.Especialidad;
import com.saludtotal.clinica.models.Estado;
import com.saludtotal.clinica.models.Persona;
import com.saludtotal.dto.ProfesionalDTO;
import com.saludtotal.clinica.models.Profesional;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.repositories.EspecialidadRepository;
import com.saludtotal.repositories.EstadoRepository;
import com.saludtotal.repositories.PersonaRepository;
import com.saludtotal.repositories.ProfesionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfesionalService {

    @Autowired
    private ProfesionalRepository profesionalRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    public List<ProfesionalDTO> listarTodos() {
        return profesionalRepository.findAll()
                .stream()
                .map(this::convertirAProfesionalDTO)
                .collect(Collectors.toList());
    }

    public ProfesionalDTO buscarPorId(Long id) {
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Profesional no encontrado con ID: " + id));
        return convertirAProfesionalDTO(profesional);
    }

    public ProfesionalDTO registrarProfesional(ProfesionalDTO dto) {
        // 1. Crear entidad Persona y guardarla
        Persona persona = new Persona();
        persona.setDni(dto.getDni());
        persona.setNombre(dto.getNombre());
        persona.setApellido(dto.getApellido());
        persona.setEmail(dto.getEmail());
        persona.setTelefono(dto.getTelefono());
        persona.setDireccion(dto.getDireccion());
        persona.setFechaNacimiento(dto.getFechaNacimiento());
        persona.setIdRol(dto.getIdRol());
        persona.setIdEspecialidad(dto.getIdEspecialidad());
        persona.setIdEstado(dto.getIdEstado());

        Persona personaGuardada = personaRepository.save(persona);

        // 2. Crear entidad Profesional asociada a esa persona
        Profesional profesional = new Profesional();
        profesional.setPersona(personaGuardada);

        Especialidad especialidad = new Especialidad();
        especialidad.setIdEspecialidad(dto.getIdEspecialidad());
        profesional.setEspecialidad(especialidad);

        Estado estado = new Estado();
        estado.setIdEstado(dto.getIdEstado());
        profesional.setEstado(estado);

        profesional.setMatriculaProfesional(dto.getMatriculaProfesional());

        Profesional nuevo = profesionalRepository.save(profesional);
        return convertirAProfesionalDTO(nuevo);
    }

    public ProfesionalDTO actualizarProfesional(Long id, ProfesionalDTO dto) {
        Profesional profesionalExistente = profesionalRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Profesional no encontrado con ID: " + id));

        // Actualizamos datos de Persona
        Persona personaExistente = profesionalExistente.getPersona();
        if (personaExistente == null) {
            throw new RecursoNoEncontradoException("Persona asociada no encontrada para el profesional con ID: " + id);
        }
        personaExistente.setDni(dto.getDni());
        personaExistente.setNombre(dto.getNombre());
        personaExistente.setApellido(dto.getApellido());
        personaExistente.setEmail(dto.getEmail());
        personaExistente.setTelefono(dto.getTelefono());
        personaExistente.setDireccion(dto.getDireccion());
        personaExistente.setFechaNacimiento(dto.getFechaNacimiento());
        personaExistente.setIdRol(dto.getIdRol());
        personaExistente.setIdEstado(dto.getIdEstado()); // importante también para persona
        personaExistente.setIdEspecialidad(dto.getIdEspecialidad()); // si corresponde en persona

        // Guardar persona primero (para evitar errores de integridad)
        // Asumo que tenés PersonaRepository, si no, podrías usar cascade o manejarlo diferente
        personaRepository.save(personaExistente);

        // Actualizamos campos del profesional
        profesionalExistente.setMatriculaProfesional(dto.getMatriculaProfesional());

        Especialidad especialidad = new Especialidad();
        especialidad.setIdEspecialidad(dto.getIdEspecialidad());
        profesionalExistente.setEspecialidad(especialidad);

        Estado estado = new Estado();
        estado.setIdEstado(dto.getIdEstado());
        profesionalExistente.setEstado(estado);

        Profesional actualizado = profesionalRepository.save(profesionalExistente);

        return convertirAProfesionalDTO(actualizado);
    }


    public void eliminarProfesional(Long id) {
        Profesional profesional = profesionalRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Profesional no encontrado con ID: " + id));
        profesionalRepository.delete(profesional);
    }

    // Convertir entidad a DTO
    private ProfesionalDTO convertirAProfesionalDTO(Profesional profesional) {
        ProfesionalDTO dto = new ProfesionalDTO();
        dto.setId(profesional.getIdProfesional());

        Persona persona = profesional.getPersona();
        dto.setIdPersona(persona.getId());
        dto.setDni(persona.getDni());
        dto.setNombre(persona.getNombre());
        dto.setApellido(persona.getApellido());
        dto.setEmail(persona.getEmail());
        dto.setTelefono(persona.getTelefono());
        dto.setDireccion(persona.getDireccion());
        dto.setFechaNacimiento(persona.getFechaNacimiento());
        dto.setIdRol(persona.getIdRol());

        dto.setIdEspecialidad(profesional.getEspecialidad().getIdEspecialidad());
        dto.setMatriculaProfesional(profesional.getMatriculaProfesional());
        dto.setIdEstado(profesional.getEstado().getIdEstado());

        return dto;
    }

    // Convertir DTO a entidad
    private Profesional convertirAProfesional(ProfesionalDTO dto) {
        Profesional profesional = new Profesional();
        profesional.setIdProfesional(dto.getId());

        Persona persona = new Persona();
        persona.setDni(dto.getDni());
        persona.setNombre(dto.getNombre());
        persona.setApellido(dto.getApellido());
        persona.setEmail(dto.getEmail());
        persona.setTelefono(dto.getTelefono());
        persona.setDireccion(dto.getDireccion());
        persona.setFechaNacimiento(dto.getFechaNacimiento());
        persona.setIdRol(dto.getIdRol());
        persona.setIdEspecialidad(dto.getIdEspecialidad());
        persona.setIdEstado(dto.getIdEstado()); // ✅ Este campo es clave

        profesional.setPersona(persona);

        Especialidad especialidad = new Especialidad();
        especialidad.setIdEspecialidad(dto.getIdEspecialidad());
        profesional.setEspecialidad(especialidad);

        Estado estado = new Estado();
        estado.setIdEstado(dto.getIdEstado());
        profesional.setEstado(estado);

        profesional.setMatriculaProfesional(dto.getMatriculaProfesional());

        return profesional;
    }

}
