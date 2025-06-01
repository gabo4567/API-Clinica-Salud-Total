package com.saludtotal.service;

import com.saludtotal.dto.ConsultaRequestDTO;
import com.saludtotal.dto.ConsultaResponseDTO;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.clinica.models.Consulta;
import com.saludtotal.clinica.models.Estado;
import com.saludtotal.repositories.ConsultaRepository;
import com.saludtotal.repositories.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    // 1. Crear consulta
    public ConsultaResponseDTO crearConsulta(ConsultaRequestDTO dto) {
        validarConsulta(dto);

        Estado estado = estadoRepository.findById(dto.getIdEstado().longValue())
                .orElseThrow(() -> new RecursoNoEncontradoException("Estado con ID " + dto.getIdEstado() + " no encontrado"));

        // Usa el constructor con parámetros, id = null porque es nuevo
        Consulta consulta = new Consulta(null, dto.getNombre(), dto.getCorreo(), dto.getMensaje(), estado);

        Consulta guardada = consultaRepository.save(consulta);

        // Usa el constructor DTO que recibe Consulta directamente
        return new ConsultaResponseDTO(guardada);
    }

    // 2. Listar todas las consultas
    public List<ConsultaResponseDTO> listarConsultas() {
        return consultaRepository.findAll()
                .stream()
                .map(ConsultaResponseDTO::new) // constructor que recibe Consulta
                .collect(Collectors.toList());
    }

    // 3. Obtener consulta por ID
    public ConsultaResponseDTO obtenerConsultaPorId(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Consulta con ID " + id + " no encontrada"));
        return new ConsultaResponseDTO(consulta);
    }

    // 4. Eliminar consulta
    public void eliminarConsulta(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Consulta con ID " + id + " no encontrada"));
        consultaRepository.delete(consulta);
    }

    // ✔️ Validaciones
    private void validarConsulta(ConsultaRequestDTO dto) {
        if (dto.getCorreo() == null || !dto.getCorreo().matches("^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Correo electrónico inválido");
        }
        if (dto.getMensaje() == null || dto.getMensaje().trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío");
        }
        if (dto.getMensaje().length() > 1000) {
            throw new IllegalArgumentException("El mensaje excede el máximo de 1000 caracteres");
        }
        if (dto.getIdEstado() == null) {
            throw new IllegalArgumentException("Debe especificar el ID del estado");
        }
    }
}
