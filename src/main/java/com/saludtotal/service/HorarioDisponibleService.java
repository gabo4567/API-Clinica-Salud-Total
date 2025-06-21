package com.saludtotal.service;

import com.saludtotal.clinica.models.HorarioDisponible;
import com.saludtotal.dto.HorarioDisponibleDTO;
import com.saludtotal.repositories.HorarioDisponibleRepository;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HorarioDisponibleService {

    private final HorarioDisponibleRepository horarioDisponibleRepository;

    public HorarioDisponibleService(HorarioDisponibleRepository horarioDisponibleRepository) {
        this.horarioDisponibleRepository = horarioDisponibleRepository;
    }

    // Map robusto para convertir enum DiaSemana a DayOfWeek
    private static final Map<HorarioDisponible.DiaSemana, DayOfWeek> DIA_SEMANA_MAP = new HashMap<>();
    static {
        DIA_SEMANA_MAP.put(HorarioDisponible.DiaSemana.Lunes, DayOfWeek.MONDAY);
        DIA_SEMANA_MAP.put(HorarioDisponible.DiaSemana.Martes, DayOfWeek.TUESDAY);
        DIA_SEMANA_MAP.put(HorarioDisponible.DiaSemana.Miercoles, DayOfWeek.WEDNESDAY);
        DIA_SEMANA_MAP.put(HorarioDisponible.DiaSemana.Jueves, DayOfWeek.THURSDAY);
        DIA_SEMANA_MAP.put(HorarioDisponible.DiaSemana.Viernes, DayOfWeek.FRIDAY);
        DIA_SEMANA_MAP.put(HorarioDisponible.DiaSemana.Sabado, DayOfWeek.SATURDAY);
        DIA_SEMANA_MAP.put(HorarioDisponible.DiaSemana.Domingo, DayOfWeek.SUNDAY);
    }

    public List<HorarioDisponibleDTO> obtenerHorariosDisponiblesPorProfesional(Long idProfesional) {
        Long idEstadoActivo = 1L;

        List<HorarioDisponible> horarios = horarioDisponibleRepository.findByIdProfesionalAndIdEstado(idProfesional, idEstadoActivo);
        LocalDate hoy = LocalDate.now();
        LocalDate fin = hoy.plusDays(14);

        List<HorarioDisponibleDTO> resultado = new ArrayList<>();

        for (LocalDate fecha = hoy; !fecha.isAfter(fin); fecha = fecha.plusDays(1)) {
            DayOfWeek dayOfWeek = fecha.getDayOfWeek();

            // Imprimir fecha y día de la semana que se está evaluando
            System.out.println("Evaluando fecha: " + fecha + " - Día de semana: " + dayOfWeek);

            // Imprimir cada horario y su mapeo
            for (HorarioDisponible h : horarios) {
                HorarioDisponible.DiaSemana diaSemana = h.getDiaSemana();
                DayOfWeek diaMapeado = DIA_SEMANA_MAP.get(diaSemana);
                System.out.println("Horario en BD: '" + diaSemana + "' -> Mapeado a: " + diaMapeado);
            }

            List<HorarioDisponible> horariosDelDia = horarios.stream()
                    .filter(h -> {
                        DayOfWeek dia = DIA_SEMANA_MAP.get(h.getDiaSemana());
                        return dia != null && dia.equals(dayOfWeek);
                    })
                    .collect(Collectors.toList());

            if (!horariosDelDia.isEmpty()) {
                List<String> horasDisponibles = new ArrayList<>();

                for (HorarioDisponible hd : horariosDelDia) {
                    LocalTime horaInicio = hd.getHoraInicio();
                    LocalTime horaFin = hd.getHoraFin();
                    LocalTime tempHora = horaInicio;

                    while (!tempHora.isAfter(horaFin.minusMinutes(30))) {
                        horasDisponibles.add(tempHora.format(DateTimeFormatter.ofPattern("HH:mm")));
                        tempHora = tempHora.plusMinutes(30);
                    }
                }

                horasDisponibles = horasDisponibles.stream().distinct().sorted().collect(Collectors.toList());
                resultado.add(new HorarioDisponibleDTO(fecha, horasDisponibles));
            }
        }

        return resultado;
    }
}

