package com.saludtotal.controller;

import com.saludtotal.dto.HorarioDisponibleDTO;
import com.saludtotal.service.HorarioDisponibleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horarios-disponibles")
@CrossOrigin(origins = "*") // Ajusta los orígenes según necesidad de seguridad
public class HorarioDisponibleController {

    private final HorarioDisponibleService horarioDisponibleService;

    public HorarioDisponibleController(HorarioDisponibleService horarioDisponibleService) {
        this.horarioDisponibleService = horarioDisponibleService;
    }

    // Endpoint para obtener horarios disponibles por idProfesional
    @GetMapping("/{idProfesional}")
    public ResponseEntity<List<HorarioDisponibleDTO>> obtenerHorariosPorProfesional(@PathVariable Long idProfesional) {
        List<HorarioDisponibleDTO> horariosDTO = horarioDisponibleService.obtenerHorariosDisponiblesPorProfesional(idProfesional);
        if (horariosDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(horariosDTO);
    }
}