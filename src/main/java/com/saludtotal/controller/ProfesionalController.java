package com.saludtotal.controller;

import com.saludtotal.dto.ProfesionalDTO;
import com.saludtotal.service.ProfesionalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profesionales")
public class ProfesionalController {

    @Autowired
    private ProfesionalService profesionalService;

    @GetMapping
    public ResponseEntity<List<ProfesionalDTO>> listarTodos() {
        return ResponseEntity.ok(profesionalService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesionalDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(profesionalService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProfesionalDTO> registrarProfesional(@Valid @RequestBody ProfesionalDTO dto) {
        ProfesionalDTO nuevo = profesionalService.registrarProfesional(dto);
        return ResponseEntity.ok(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfesionalDTO> actualizarProfesional(@PathVariable Long id, @Valid @RequestBody ProfesionalDTO dto) {
        ProfesionalDTO actualizado = profesionalService.actualizarProfesional(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProfesional(@PathVariable Long id) {
        profesionalService.eliminarProfesional(id);
        return ResponseEntity.noContent().build();
    }
}
