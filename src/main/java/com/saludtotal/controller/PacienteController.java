package com.saludtotal.controller;

import com.saludtotal.clinica.models.Paciente;
import com.saludtotal.dto.LoginRequestDTO;
import com.saludtotal.dto.RegistroPacienteDTO;
import com.saludtotal.repositories.PacienteRepository;
import com.saludtotal.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    @Autowired
    private PacienteRepository pacienteRepository;

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
        System.out.println("LLEGÓ AL CONTROLLER actualizarPaciente con id=" + id);
        Paciente pacienteActualizado = pacienteService.actualizarPaciente(id, dto);
        return ResponseEntity.ok(pacienteActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id) {
        System.out.println(">>> Llega al backend solicitud para eliminar paciente con ID: " + id);

        Paciente paciente = pacienteRepository.findById(id).orElse(null);

        if (paciente == null) {
            System.out.println(">>> No existe paciente con ID: " + id);
            return ResponseEntity.notFound().build();
        }

        System.out.println(">>> Paciente encontrado: ID=" + paciente.getId() + ", Estado actual=" + paciente.getIdEstado());

        paciente.setIdEstado(2L); // Marcar inactivo

        pacienteRepository.saveAndFlush(paciente);

        System.out.println(">>> Paciente con ID " + id + " marcado como inactivo.");

        return ResponseEntity.noContent().build();
    }



}
