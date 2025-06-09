package com.saludtotal.controller;

import com.saludtotal.dto.SecretariaDTO;
import com.saludtotal.service.SecretariaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/secretarias")
public class SecretariaController {

    @Autowired
    private SecretariaService secretariaService;

    @GetMapping
    public ResponseEntity<List<SecretariaDTO>> listarTodas() {
        return ResponseEntity.ok(secretariaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecretariaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(secretariaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<SecretariaDTO> registrarSecretaria(@Valid @RequestBody SecretariaDTO dto) {
        SecretariaDTO nueva = secretariaService.registrarSecretaria(dto);
        return ResponseEntity.ok(nueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SecretariaDTO> actualizarSecretaria(@PathVariable Long id, @Valid @RequestBody SecretariaDTO dto) {
        SecretariaDTO actualizada = secretariaService.actualizarSecretaria(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSecretaria(@PathVariable Long id) {
        secretariaService.eliminarSecretaria(id);
        return ResponseEntity.noContent().build();
    }
}
