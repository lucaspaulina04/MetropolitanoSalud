package com.saludmetropolitana.gestionpacientes.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A AgendaMedica.
 */
@Entity
@Table(name = "agenda_medica")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgendaMedica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_hora")
    private String fechaHora;

    @Column(name = "paciente_id")
    private String pacienteID;

    @Column(name = "medico_id")
    private String medicoID;

    @Column(name = "centro_salud_id")
    private String centroSaludID;

    @Column(name = "estado_hora")
    private String estadoHora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "pacientes" }, allowSetters = true)
    private Paciente pacienteHora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "horaMedicos" }, allowSetters = true)
    private Medicos horasMedicas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "horaCentroSaluds" }, allowSetters = true)
    private CentroSalud horasCentroSalud;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AgendaMedica id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFechaHora() {
        return this.fechaHora;
    }

    public AgendaMedica fechaHora(String fechaHora) {
        this.setFechaHora(fechaHora);
        return this;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getPacienteID() {
        return this.pacienteID;
    }

    public AgendaMedica pacienteID(String pacienteID) {
        this.setPacienteID(pacienteID);
        return this;
    }

    public void setPacienteID(String pacienteID) {
        this.pacienteID = pacienteID;
    }

    public String getMedicoID() {
        return this.medicoID;
    }

    public AgendaMedica medicoID(String medicoID) {
        this.setMedicoID(medicoID);
        return this;
    }

    public void setMedicoID(String medicoID) {
        this.medicoID = medicoID;
    }

    public String getCentroSaludID() {
        return this.centroSaludID;
    }

    public AgendaMedica centroSaludID(String centroSaludID) {
        this.setCentroSaludID(centroSaludID);
        return this;
    }

    public void setCentroSaludID(String centroSaludID) {
        this.centroSaludID = centroSaludID;
    }

    public String getEstadoHora() {
        return this.estadoHora;
    }

    public AgendaMedica estadoHora(String estadoHora) {
        this.setEstadoHora(estadoHora);
        return this;
    }

    public void setEstadoHora(String estadoHora) {
        this.estadoHora = estadoHora;
    }

    public Paciente getPacienteHora() {
        return this.pacienteHora;
    }

    public void setPacienteHora(Paciente paciente) {
        this.pacienteHora = paciente;
    }

    public AgendaMedica pacienteHora(Paciente paciente) {
        this.setPacienteHora(paciente);
        return this;
    }

    public Medicos getHorasMedicas() {
        return this.horasMedicas;
    }

    public void setHorasMedicas(Medicos medicos) {
        this.horasMedicas = medicos;
    }

    public AgendaMedica horasMedicas(Medicos medicos) {
        this.setHorasMedicas(medicos);
        return this;
    }

    public CentroSalud getHorasCentroSalud() {
        return this.horasCentroSalud;
    }

    public void setHorasCentroSalud(CentroSalud centroSalud) {
        this.horasCentroSalud = centroSalud;
    }

    public AgendaMedica horasCentroSalud(CentroSalud centroSalud) {
        this.setHorasCentroSalud(centroSalud);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgendaMedica)) {
            return false;
        }
        return getId() != null && getId().equals(((AgendaMedica) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgendaMedica{" +
            "id=" + getId() +
            ", fechaHora='" + getFechaHora() + "'" +
            ", pacienteID='" + getPacienteID() + "'" +
            ", medicoID='" + getMedicoID() + "'" +
            ", centroSaludID='" + getCentroSaludID() + "'" +
            ", estadoHora='" + getEstadoHora() + "'" +
            "}";
    }
}
