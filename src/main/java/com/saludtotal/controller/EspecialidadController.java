package com.saludtotal.controller;

import com.saludtotal.dto.EspecialidadDTO;
import com.saludtotal.service.EspecialidadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    @Autowired
    private EspecialidadService especialidadService;

    @GetMapping
    public ResponseEntity<List<EspecialidadDTO>> listarTodos() {
        return ResponseEntity.ok(especialidadService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<EspecialidadDTO> registrarEspecialidad(@Valid @RequestBody EspecialidadDTO dto) {
        EspecialidadDTO nueva = especialidadService.registrarEspecialidad(dto);
        return ResponseEntity.ok(nueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadDTO> actualizarEspecialidad(@PathVariable Long id, @Valid @RequestBody EspecialidadDTO dto) {
        EspecialidadDTO actualizada = especialidadService.actualizarEspecialidad(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEspecialidad(@PathVariable Long id) {
        especialidadService.eliminarEspecialidad(id);
        return ResponseEntity.noContent().build();
    }
}
