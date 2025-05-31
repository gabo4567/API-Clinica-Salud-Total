package com.saludtotal.repositories;

import com.saludtotal.clinica.models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
    boolean existsByDni(String dni);
}

