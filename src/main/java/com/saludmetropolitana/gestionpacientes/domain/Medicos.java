package com.saludmetropolitana.gestionpacientes.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Medicos.
 */
@Entity
@Table(name = "medicos")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Medicos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "especialidad")
    private String especialidad;

    @Column(name = "email")
    private String email;

    @Column(name = "telefono")
    private String telefono;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "horasMedicas")
    @JsonIgnoreProperties(value = { "pacienteHora", "horasMedicas", "horasCentroSalud" }, allowSetters = true)
    private Set<AgendaMedica> horaMedicos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Medicos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Medicos nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Medicos apellido(String apellido) {
        this.setApellido(apellido);
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEspecialidad() {
        return this.especialidad;
    }

    public Medicos especialidad(String especialidad) {
        this.setEspecialidad(especialidad);
        return this;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getEmail() {
        return this.email;
    }

    public Medicos email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Medicos telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Set<AgendaMedica> getHoraMedicos() {
        return this.horaMedicos;
    }

    public void setHoraMedicos(Set<AgendaMedica> agendaMedicas) {
        if (this.horaMedicos != null) {
            this.horaMedicos.forEach(i -> i.setHorasMedicas(null));
        }
        if (agendaMedicas != null) {
            agendaMedicas.forEach(i -> i.setHorasMedicas(this));
        }
        this.horaMedicos = agendaMedicas;
    }

    public Medicos horaMedicos(Set<AgendaMedica> agendaMedicas) {
        this.setHoraMedicos(agendaMedicas);
        return this;
    }

    public Medicos addHoraMedico(AgendaMedica agendaMedica) {
        this.horaMedicos.add(agendaMedica);
        agendaMedica.setHorasMedicas(this);
        return this;
    }

    public Medicos removeHoraMedico(AgendaMedica agendaMedica) {
        this.horaMedicos.remove(agendaMedica);
        agendaMedica.setHorasMedicas(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medicos)) {
            return false;
        }
        return getId() != null && getId().equals(((Medicos) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medicos{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", especialidad='" + getEspecialidad() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefono='" + getTelefono() + "'" +
            "}";
    }
}
