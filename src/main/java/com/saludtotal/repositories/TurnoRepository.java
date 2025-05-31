package com.saludtotal.repositories;

import com.saludtotal.clinica.models.Turno;
import com.saludtotal.clinica.models.Estado;
import com.saludtotal.clinica.models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TurnoRepository extends JpaRepository<Turno, Long> {

    // Turnos futuros para un paciente
    List<Turno> findByPacienteIdAndFechaHoraAfter(Integer pacienteId, LocalDateTime fechaHora);

    // Turnos pasados para un paciente
    List<Turno> findByPacienteIdAndFechaHoraBefore(Integer pacienteId, LocalDateTime fechaHora);

    // Todos los turnos por paciente
    List<Turno> findByPaciente(Persona paciente);

    // Todos los turnos por profesional
    List<Turno> findByProfesional(Persona profesional);

    // Turnos por profesional y fecha (por disponibilidad, etc.)
    List<Turno> findByProfesionalAndFechaHoraBetween(Persona profesional, LocalDateTime inicio, LocalDateTime fin);

    // Turnos por profesional y estado
    List<Turno> findByProfesionalAndEstado(Persona profesional, Estado estado);

    // Turnos por estado
    List<Turno> findByEstado(Estado estado);

    // Turnos por fecha exacta
    List<Turno> findByFechaHora(LocalDateTime fechaHora);

    // Turnos dentro de un rango de fecha y hora (sin filtrar por profesional)
    List<Turno> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    // Turnos por paciente y estado
    List<Turno> findByPacienteAndEstado(Persona paciente, Estado estado);

    // Turnos atendidos por profesional en un rango de fechas
    long countByProfesionalAndEstadoAndFechaHoraBetween(Persona profesional, Estado estado, LocalDateTime inicio, LocalDateTime fin);
}
