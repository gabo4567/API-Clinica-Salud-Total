package com.saludtotal.clinica.models;

import jakarta.persistence.*;

@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @Column(name = "id_rol")
    private Long idRol;

    @Column(length = 100, nullable = false)
    private String nombre;

    @Column(length = 200)
    private String descripcion;

    public Rol() {}

    public Rol(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y setters
    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
