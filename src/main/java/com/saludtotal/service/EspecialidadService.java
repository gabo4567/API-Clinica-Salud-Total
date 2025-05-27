package com.saludtotal.service;

import com.saludtotal.dto.EspecialidadDTO;
import com.saludtotal.clinica.models.Especialidad;
import com.saludtotal.clinica.models.Estado;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.repositories.EspecialidadRepository;
import com.saludtotal.repositories.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EspecialidadService {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    public List<EspecialidadDTO> listarTodos() {
        return especialidadRepository.findAll()
                .stream()
                .map(this::convertirAEspecialidadDTO)
                .collect(Collectors.toList());
    }

    public EspecialidadDTO buscarPorId(Long id) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Especialidad no encontrada con ID: " + id));
        return convertirAEspecialidadDTO(especialidad);
    }

    public EspecialidadDTO registrarEspecialidad(EspecialidadDTO dto) {
        Especialidad especialidad = convertirAEspecialidad(dto);
        Especialidad nueva = especialidadRepository.save(especialidad);
        return convertirAEspecialidadDTO(nueva);
    }

    public EspecialidadDTO actualizarEspecialidad(Long id, EspecialidadDTO dto) {
        Especialidad especialidadExistente = especialidadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Especialidad no encontrada con ID: " + id));
        especialidadExistente.setNombre(dto.getNombre());
        especialidadExistente.setDescripcion(dto.getDescripcion());

        Estado estado = estadoRepository.findById(dto.getIdEstado())
                .orElseThrow(() -> new RecursoNoEncontradoException("Estado no encontrado con ID: " + dto.getIdEstado()));
        especialidadExistente.setEstado(estado);

        Especialidad actualizada = especialidadRepository.save(especialidadExistente);
        return convertirAEspecialidadDTO(actualizada);
    }

    public void eliminarEspecialidad(Long id) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Especialidad no encontrada con ID: " + id));
        especialidadRepository.delete(especialidad);
    }

    // Convertir entidad a DTO
    private EspecialidadDTO convertirAEspecialidadDTO(Especialidad especialidad) {
        EspecialidadDTO dto = new EspecialidadDTO();
        dto.setIdEspecialidad(especialidad.getIdEspecialidad());
        dto.setNombre(especialidad.getNombre());
        dto.setDescripcion(especialidad.getDescripcion());
        if (especialidad.getEstado() != null) {
            dto.setIdEstado(especialidad.getEstado().getIdEstado());
        }
        return dto;
    }

    // Convertir DTO a entidad
    private Especialidad convertirAEspecialidad(EspecialidadDTO dto) {
        Especialidad especialidad = new Especialidad();
        especialidad.setIdEspecialidad(dto.getIdEspecialidad());
        especialidad.setNombre(dto.getNombre());
        especialidad.setDescripcion(dto.getDescripcion());

        if (dto.getIdEstado() != null) {
            Estado estado = new Estado();
            estado.setIdEstado(dto.getIdEstado());
            especialidad.setEstado(estado);
        }

        return especialidad;
    }

}
