package com.saludtotal.repositories;

import com.saludtotal.clinica.models.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
    // Consultas personalizadas en caso de ser necesarias
}
