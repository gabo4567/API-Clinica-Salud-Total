package com.saludtotal.repositories;

import com.saludtotal.clinica.models.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

}

