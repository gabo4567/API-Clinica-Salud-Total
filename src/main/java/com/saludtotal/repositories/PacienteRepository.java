package com.saludtotal.repositories;

import com.saludtotal.clinica.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    boolean existsByPersonaDni(String dni);
    Optional<Paciente> findByPersonaDni(String dni);
    List<Paciente> findByPersonaNombreContainingIgnoreCase(String nombre);
    List<Paciente> findByPersonaApellidoContainingIgnoreCase(String apellido);

}
