package com.saludmetropolitana.gestionpacientes.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A CentroSalud.
 */
@Entity
@Table(name = "centro_salud")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CentroSalud implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "tipo")
    private String tipo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "horasCentroSalud")
    @JsonIgnoreProperties(value = { "pacienteHora", "horasMedicas", "horasCentroSalud" }, allowSetters = true)
    private Set<AgendaMedica> horaCentroSaluds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CentroSalud id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public CentroSalud nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public CentroSalud direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public CentroSalud telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipo() {
        return this.tipo;
    }

    public CentroSalud tipo(String tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Set<AgendaMedica> getHoraCentroSaluds() {
        return this.horaCentroSaluds;
    }

    public void setHoraCentroSaluds(Set<AgendaMedica> agendaMedicas) {
        if (this.horaCentroSaluds != null) {
            this.horaCentroSaluds.forEach(i -> i.setHorasCentroSalud(null));
        }
        if (agendaMedicas != null) {
            agendaMedicas.forEach(i -> i.setHorasCentroSalud(this));
        }
        this.horaCentroSaluds = agendaMedicas;
    }

    public CentroSalud horaCentroSaluds(Set<AgendaMedica> agendaMedicas) {
        this.setHoraCentroSaluds(agendaMedicas);
        return this;
    }

    public CentroSalud addHoraCentroSalud(AgendaMedica agendaMedica) {
        this.horaCentroSaluds.add(agendaMedica);
        agendaMedica.setHorasCentroSalud(this);
        return this;
    }

    public CentroSalud removeHoraCentroSalud(AgendaMedica agendaMedica) {
        this.horaCentroSaluds.remove(agendaMedica);
        agendaMedica.setHorasCentroSalud(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CentroSalud)) {
            return false;
        }
        return getId() != null && getId().equals(((CentroSalud) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CentroSalud{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", tipo='" + getTipo() + "'" +
            "}";
    }
}
