package com.saludtotal.repositories;

import com.saludtotal.clinica.models.Profesional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesionalRepository extends JpaRepository<Profesional, Long> {
    // Aquí podés agregar consultas personalizadas si las necesitas luego
}
