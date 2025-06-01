package com.saludtotal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ConsultaRequestDTO {

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "Debe proporcionar un correo electrónico válido.")
    @Size(max = 100, message = "El correo no puede superar los 100 caracteres.")
    private String correo;

    @NotBlank(message = "El mensaje no puede estar vacío.")
    @Size(max = 1000, message = "El mensaje no puede superar los 1000 caracteres.")
    private String mensaje;

    @NotNull(message = "Debe especificar el ID del estado.")
    private Integer idEstado;

    public ConsultaRequestDTO() {
    }

    public ConsultaRequestDTO(String nombre, String correo, String mensaje, Integer idEstado) {
        this.nombre = nombre;
        this.correo = correo;
        this.mensaje = mensaje;
        this.idEstado = idEstado;
    }

    // Getters y Setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }

}
