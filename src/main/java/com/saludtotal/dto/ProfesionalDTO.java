package com.saludtotal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProfesionalDTO {

    @NotNull(message = "El id del profesional es obligatorio")
    private Long id;

    @NotNull(message = "El id de la persona es obligatorio")
    private Long idPersona;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasenia;

    @NotNull(message = "El id de la especialidad es obligatorio")
    private Long idEspecialidad;

    @NotBlank(message = "La matrícula profesional es obligatoria")
    private String matriculaProfesional;

    @NotNull(message = "El id del estado es obligatorio")
    private Long idEstado;

    // Constructor vacío
    public ProfesionalDTO() {
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    public Long getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(Long idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public String getMatriculaProfesional() {
        return matriculaProfesional;
    }

    public void setMatriculaProfesional(String matriculaProfesional) {this.matriculaProfesional = matriculaProfesional;}

    public Long getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Long idEstado) {
        this.idEstado = idEstado;
    }

    public String getContrasenia() {return contrasenia;}

    public void setContrasenia(String contrasenia) {this.contrasenia = contrasenia;}
}
