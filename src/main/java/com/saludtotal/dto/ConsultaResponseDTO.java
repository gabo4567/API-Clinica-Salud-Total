package com.saludtotal.dto;

import com.saludtotal.clinica.models.Consulta;

import java.time.LocalDateTime;

public class ConsultaResponseDTO {

    private Long id;
    private String nombre;
    private String correo;
    private String mensaje;
    private LocalDateTime fecha;
    private String estado;


    public ConsultaResponseDTO(Long id, String nombre, String correo, String mensaje, LocalDateTime fecha, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.estado = estado;
    }


    public ConsultaResponseDTO(Consulta consulta) {
        this.id = consulta.getId();
        this.nombre = consulta.getNombre();
        this.correo = consulta.getCorreo();
        this.mensaje = consulta.getMensaje();
        this.fecha = consulta.getFecha();
        this.estado = consulta.getEstado().getNombre();
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

