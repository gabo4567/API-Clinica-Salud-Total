package com.saludtotal.controller;

import com.saludtotal.dto.ConsultaRequestDTO;
import com.saludtotal.dto.ConsultaResponseDTO;
import com.saludtotal.service.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    // 1. Crear consulta (solo pacientes autenticados)
    @PostMapping
    public ResponseEntity<ConsultaResponseDTO> crearConsulta(@Valid @RequestBody ConsultaRequestDTO dto) {
        ConsultaResponseDTO respuesta = consultaService.crearConsulta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    // 2. Listar todas las consultas (solo secretaria)
    @GetMapping
    public ResponseEntity<List<ConsultaResponseDTO>> listarConsultas() {
        List<ConsultaResponseDTO> lista = consultaService.listarConsultas();
        return ResponseEntity.ok(lista);
    }

    // 3. Obtener consulta por ID (solo secretaria)
    @GetMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> obtenerConsultaPorId(@PathVariable Long id) {
        ConsultaResponseDTO consulta = consultaService.obtenerConsultaPorId(id);
        return ResponseEntity.ok(consulta);
    }

    // 4. Eliminar consulta (solo secretaria)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarConsulta(@PathVariable Long id) {
        consultaService.eliminarConsulta(id);
        return ResponseEntity.noContent().build();
    }
}


