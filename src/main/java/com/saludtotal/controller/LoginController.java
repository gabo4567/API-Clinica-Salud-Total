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

    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = loginService.login(request.getNombreUsuario(), request.getContrasena());
        return ResponseEntity.ok(response);
    }
}
