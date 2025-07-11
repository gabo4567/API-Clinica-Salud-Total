package com.saludtotal.clinica.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_persona", referencedColumnName = "id_persona", nullable = false)
    private Persona persona;

    @Column(name = "obra_social")
    @NotBlank(message = "La obra social no puede estar vacía")
    private String obraSocial;

    @Column(name = "id_estado")
    private Long idEstado;

    // Constructor vacío
    public Paciente() {
    }

    public Paciente(Persona persona, String obraSocial, Long idEstado) {
        this.persona = persona;
        this.obraSocial = obraSocial;
        this.idEstado = idEstado;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(String obraSocial) {
        this.obraSocial = obraSocial;
    }

    public Long getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Long idEstado) {
        this.idEstado = idEstado;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

}
