package com.saludtotal.repositories;

import com.saludtotal.clinica.models.Profesional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfesionalRepository extends JpaRepository<Profesional, Long> {
    @Query("SELECT p FROM Profesional p WHERE p.persona.email = :email")
    Optional<Profesional> findByEmail(@Param("email") String email);
}
