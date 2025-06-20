package com.saludtotal.repositories;

import com.saludtotal.clinica.models.Turno;
import com.saludtotal.clinica.models.Estado;
import com.saludtotal.clinica.models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TurnoRepository extends JpaRepository<Turno, Long> {

    // Turnos futuros para un paciente
    List<Turno> findByPacienteIdAndFechaHoraAfter(Long pacienteId, LocalDateTime fechaHora);

    // Turnos pasados para un paciente
    List<Turno> findByPacienteIdAndFechaHoraBefore(Long pacienteId, LocalDateTime fechaHora);

    // Todos los turnos por paciente
    List<Turno> findById(Persona paciente);

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

    // Turnos cancelados y reprogramados
    long countByEstadoAndFechaHoraBetween(Estado estado, LocalDateTime inicio, LocalDateTime fin);

    long countByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    // Metodo para obtener el último comprobante registrado, ordenando por comprobante descendente y limitando a 1
    @Query(value = "SELECT t.comprobante FROM Turno t ORDER BY t.comprobante DESC")
    List<String> findLastComprobante(Pageable pageable);

    @Query(value = "SELECT MAX(comprobante) FROM turno WHERE comprobante LIKE :prefix%", nativeQuery = true)
    String findMaxComprobanteByPrefix(@Param("prefix") String prefix);


    // Datos de cancelación por especialidad en un rango de fechas
    @Query(value = "SELECT " +
            "e.nombre AS especialidad, " +
            "COUNT(t.id_turno) AS totalTurnos, " +
            "SUM(CASE WHEN est.nombre = 'Cancelado' AND est.id_entidad = 5 THEN 1 ELSE 0 END) AS turnosCancelados " +
            "FROM turno t " +
            "JOIN persona p ON t.id_profesional = p.id_persona " +  // CAMBIO ACÁ
            "JOIN especialidad e ON p.id_especialidad = e.id_especialidad " +
            "JOIN estado est ON t.id_estado = est.id_estado " +
            "WHERE p.id_rol = 3 " +                                // FILTRAMOS SOLO PROFESIONALES
            "AND t.fecha_hora BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY e.nombre", nativeQuery = true)
    List<Object[]> obtenerDatosCancelacionPorEspecialidad(@Param("fechaInicio") LocalDate fechaInicio,
                                                          @Param("fechaFin") LocalDate fechaFin);
}
