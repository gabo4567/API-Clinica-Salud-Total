package com.saludtotal.repositories;

import com.saludtotal.clinica.models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SecretariaRepository extends JpaRepository<Persona, Long> {

    @Query("SELECT p FROM Persona p WHERE p.idRol = 2")
    List<Persona> findAllSecretarias();

    @Query("SELECT p FROM Persona p WHERE p.id = :id AND p.idRol = 2")
    Optional<Persona> findSecretariaById(@Param("id") Long id);

    @Query("SELECT p FROM Persona p WHERE p.email = :email AND p.idRol = 2")
    Optional<Persona> findByEmail(@Param("email") String email);
}

