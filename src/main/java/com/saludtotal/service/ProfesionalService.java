package com.saludtotal.service;

import com.saludtotal.clinica.models.Especialidad;
import com.saludtotal.clinica.models.Estado;
import com.saludtotal.clinica.models.Persona;
import com.saludtotal.dto.ProfesionalDTO;
import com.saludtotal.clinica.models.Profesional;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.repositories.ProfesionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfesionalService {

    @Autowired
    private ProfesionalRepository profesionalRepository;

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
        Profesional profesional = convertirAProfesional(dto);
        Profesional nuevo = profesionalRepository.save(profesional);
        return convertirAProfesionalDTO(nuevo);
    }

    public ProfesionalDTO actualizarProfesional(Long id, ProfesionalDTO dto) {
        Profesional profesionalExistente = profesionalRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Profesional no encontrado con ID: " + id));

        profesionalExistente.setMatriculaProfesional(dto.getMatriculaProfesional());

        Especialidad especialidad = new Especialidad();
        especialidad.setIdEspecialidad(dto.getIdEspecialidad());
        profesionalExistente.setEspecialidad(especialidad);

        Estado estado = new Estado();
        estado.setIdEstado(dto.getIdEstado());
        profesionalExistente.setEstado(estado);

        Persona persona = new Persona();
        persona.setId(dto.getIdPersona());
        persona.setContrasenia(dto.getContrasenia()); // 👈 AGREGADO
        profesionalExistente.setPersona(persona);

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
        dto.setContrasenia(persona.getContrasenia()); // Si no querés devolverla, podés comentarla
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
        persona.setId(dto.getIdPersona());
        persona.setContrasenia(dto.getContrasenia());
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
