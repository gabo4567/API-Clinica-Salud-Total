package com.saludtotal.repositories;

import com.saludtotal.clinica.models.HorarioDisponible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioDisponibleRepository extends JpaRepository<HorarioDisponible, Long> {

    // Obtener horarios activos de un profesional
    List<HorarioDisponible> findByIdProfesionalAndIdEstado(Long idProfesional, Long idEstado);

    // Obtener horarios activos de un profesional para un día específico de la semana
    List<HorarioDisponible> findByIdProfesionalAndDiaSemanaAndIdEstado(Long idProfesional, String diaSemana, Long idEstado);

    // Obtener todos los horarios activos
    List<HorarioDisponible> findByIdEstado(Long idEstado);

}
