package com.saludtotal.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class RegistroPacienteDTO {

    // === Datos de Persona ===

    @NotBlank(message = "El DNI no puede estar vacío")
    private String dni;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    @NotBlank(message = "El teléfono no puede estar vacío")
    private String telefono;

    @NotBlank(message = "La dirección no puede estar vacía")
    private String direccion;

    @NotNull(message = "El ID de rol no puede ser nulo")
    private Long idRol;

    private Long idEspecialidad; // Puede ser null para pacientes

    @NotNull(message = "El ID de estado no puede ser nulo")
    private Long idEstadoPersona;

    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    private LocalDate fechaNacimiento;

    // === Datos de Paciente ===

    @NotBlank(message = "La obra social no puede estar vacía")
    private String obraSocial;

    @NotNull(message = "El ID de estado del paciente no puede ser nulo")
    private Long idEstadoPaciente;

    // === Getters y Setters ===

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Long getIdRol() { return idRol; }
    public void setIdRol(Long idRol) { this.idRol = idRol; }

    public Long getIdEspecialidad() { return idEspecialidad; }
    public void setIdEspecialidad(Long idEspecialidad) { this.idEspecialidad = idEspecialidad; }

    public Long getIdEstadoPersona() { return idEstadoPersona; }
    public void setIdEstadoPersona(Long idEstadoPersona) { this.idEstadoPersona = idEstadoPersona; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getObraSocial() { return obraSocial; }
    public void setObraSocial(String obraSocial) { this.obraSocial = obraSocial; }

    public int getIdEstadoPaciente() { return Math.toIntExact(idEstadoPaciente); }
    public void setIdEstadoPaciente(Long idEstadoPaciente) { this.idEstadoPaciente = idEstadoPaciente; }
}
