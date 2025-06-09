package com.saludtotal.service;

import com.saludtotal.clinica.models.Persona;
import com.saludtotal.clinica.models.Estado;
import com.saludtotal.clinica.models.Rol;
import com.saludtotal.dto.SecretariaDTO;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.repositories.SecretariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SecretariaService {

    @Autowired
    private SecretariaRepository secretariaRepository;

    public List<SecretariaDTO> listarTodas() {
        return secretariaRepository.findAllSecretarias()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public SecretariaDTO buscarPorId(Long id) {
        Persona secretaria = secretariaRepository.findSecretariaById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Secretaria no encontrada con ID: " + id));
        return convertirADTO(secretaria);
    }

    public SecretariaDTO registrarSecretaria(SecretariaDTO dto) {
        Persona secretaria = convertirAEntidad(dto);
        secretaria = secretariaRepository.save(secretaria);
        return convertirADTO(secretaria);
    }

    public SecretariaDTO actualizarSecretaria(Long id, SecretariaDTO dto) {
        Persona secretariaExistente = secretariaRepository.findSecretariaById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Secretaria no encontrada con ID: " + id));

        secretariaExistente.setNombre(dto.getNombre());
        secretariaExistente.setApellido(dto.getApellido());
        secretariaExistente.setEmail(dto.getEmail());
        secretariaExistente.setContrasenia(dto.getContrasenia());

        Estado estado = new Estado();
        estado.setIdEstado(dto.getIdEstado());
        secretariaExistente.setIdEstado(estado.getIdEstado());

        secretariaExistente = secretariaRepository.save(secretariaExistente);
        return convertirADTO(secretariaExistente);
    }

    public void eliminarSecretaria(Long id) {
        Persona secretaria = secretariaRepository.findSecretariaById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Secretaria no encontrada con ID: " + id));
        secretariaRepository.delete(secretaria);
    }

    // Convertir entidad a DTO
    private SecretariaDTO convertirADTO(Persona persona) {
        SecretariaDTO dto = new SecretariaDTO();
        dto.setId(persona.getId());
        dto.setNombre(persona.getNombre());
        dto.setApellido(persona.getApellido());
        dto.setEmail(persona.getEmail());
        dto.setContrasenia(persona.getContrasenia());
        dto.setIdEstado(persona.getIdEstado());
        return dto;
    }

    // Convertir DTO a entidad
    private Persona convertirAEntidad(SecretariaDTO dto) {
        Persona persona = new Persona();
        persona.setNombre(dto.getNombre());
        persona.setApellido(dto.getApellido());
        persona.setEmail(dto.getEmail());
        persona.setContrasenia(dto.getContrasenia());

        Estado estado = new Estado();
        estado.setIdEstado(dto.getIdEstado());
        persona.setIdEstado(estado.getIdEstado());

        Rol rol = new Rol();
        rol.setNombre("Secretaria");
        persona.setIdRol(2L);

        return persona;
    }
}
