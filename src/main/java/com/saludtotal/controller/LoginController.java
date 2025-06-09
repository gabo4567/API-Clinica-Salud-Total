package com.saludtotal.controller;

import com.saludtotal.dto.LoginRequestDTO;
import com.saludtotal.dto.LoginResponseDTO;
import com.saludtotal.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/paciente")
    public ResponseEntity<LoginResponseDTO> loginPaciente(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = loginService.loginPaciente(request.getEmail(), request.getContrasenia());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/profesional")
    public ResponseEntity<LoginResponseDTO> loginProfesional(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = loginService.loginProfesional(request.getEmail(), request.getContrasenia());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/secretaria")
    public ResponseEntity<LoginResponseDTO> loginSecretaria(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = loginService.loginSecretaria(request.getEmail(), request.getContrasenia());
        return ResponseEntity.ok(response);
    }

}
