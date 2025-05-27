package com.saludtotal.service;

import com.saludtotal.clinica.models.Turno;
import com.saludtotal.clinica.models.Paciente;
import com.saludtotal.exceptions.RecursoNoEncontradoException;
import com.saludtotal.repositories.PacienteRepository;
import com.saludtotal.repositories.TurnoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TurnoService {

    private final TurnoRepository turnoRepository;
    private final PacienteRepository pacienteRepository;

    public TurnoService(TurnoRepository turnoRepository, PacienteRepository pacienteRepository) {
        this.turnoRepository = turnoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    public List<Turno> obtenerTurnosFuturosPorPaciente(Integer pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId.longValue())
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado con ID: " + pacienteId));

        List<Turno> turnos = turnoRepository.findByPacienteIdAndFechaHoraAfter(pacienteId, LocalDateTime.now());
        if (turnos.isEmpty()) {
            throw new RecursoNoEncontradoException("No se encontraron turnos futuros para el paciente con ID: " + pacienteId);
        }
        return turnos;
    }

    public List<Turno> obtenerTurnosPasadosPorPaciente(Integer pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId.longValue())
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado con ID: " + pacienteId));

        List<Turno> turnos = turnoRepository.findByPacienteIdAndFechaHoraBefore(pacienteId, LocalDateTime.now());
        if (turnos.isEmpty()) {
            throw new RecursoNoEncontradoException("No se encontraron turnos pasados para el paciente con ID: " + pacienteId);
        }
        return turnos;
    }
}
