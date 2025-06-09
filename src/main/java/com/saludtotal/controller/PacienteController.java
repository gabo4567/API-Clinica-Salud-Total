package com.saludtotal.controller;

import com.saludtotal.clinica.models.Paciente;
import com.saludtotal.dto.LoginRequestDTO;
import com.saludtotal.dto.RegistroPacienteDTO;
import com.saludtotal.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping
    public ResponseEntity<Paciente> registrarPaciente(@Valid @RequestBody RegistroPacienteDTO dto) {
        Paciente nuevoPaciente = pacienteService.registrarPaciente(dto);
        return new ResponseEntity<>(nuevoPaciente, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Paciente> obtenerTodos() {
        return pacienteService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> obtenerPorId(@PathVariable Long id) {
        Paciente paciente = pacienteService.buscarPorId(id);
        return ResponseEntity.ok(paciente);
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<Paciente> obtenerPorDni(@PathVariable String dni) {
        Paciente paciente = pacienteService.buscarPorDni(dni);
        return ResponseEntity.ok(paciente);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Paciente>> obtenerPorNombre(@PathVariable String nombre) {
        List<Paciente> pacientes = pacienteService.buscarPorNombre(nombre);
        if (pacientes.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/apellido/{apellido}")
    public ResponseEntity<List<Paciente>> obtenerPorApellido(@PathVariable String apellido) {
        List<Paciente> pacientes = pacienteService.buscarPorApellido(apellido);
        if (pacientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pacientes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> actualizarPaciente(@PathVariable Long id, @Valid @RequestBody RegistroPacienteDTO dto) {
        Paciente pacienteActualizado = pacienteService.actualizarPaciente(id, dto);
        return ResponseEntity.ok(pacienteActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id) {
        pacienteService.eliminarPaciente(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Paciente> login(@RequestBody LoginRequestDTO request) {
        Optional<Paciente> paciente = pacienteService.login(request.getEmail(), request.getContrasenia());

        return paciente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
