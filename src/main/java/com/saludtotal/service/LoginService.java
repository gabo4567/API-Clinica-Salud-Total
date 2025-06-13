package com.saludtotal.service;

import com.saludtotal.clinica.models.Usuario;
import com.saludtotal.dto.LoginResponseDTO;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private com.saludtotal.repositories.UsuarioRepository usuarioRepository;

    public LoginResponseDTO login(String nombreUsuario, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNombreUsuario(nombreUsuario);

        if (usuarioOpt.isEmpty()) {
            throw new RecursoNoEncontradoException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getContrasena().equals(contrasena)) {
            throw new RecursoNoEncontradoException("Contraseña incorrecta");
        }

        return new LoginResponseDTO("Login exitoso como " + nombreUsuario);
    }
}