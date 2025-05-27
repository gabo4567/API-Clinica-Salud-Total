package com.saludtotal.repositories;

import com.saludtotal.clinica.models.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface TurnoRepository extends JpaRepository<Turno, Long> {

    // Buscar turnos futuros (fechaHora > ahora)
    List<Turno> findByPacienteIdAndFechaHoraAfter(Integer pacienteId, LocalDateTime fechaHora);

    // Buscar turnos pasados (fechaHora < ahora)
    List<Turno> findByPacienteIdAndFechaHoraBefore(Integer pacienteId, LocalDateTime fechaHora);

}
