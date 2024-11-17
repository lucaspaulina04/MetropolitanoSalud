package com.saludmetropolitana.gestionpacientes.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Paciente.
 */
@Entity
@Table(name = "paciente")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Paciente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "fechanacimiento")
    private String fechanacimiento;

    @Column(name = "edad")
    private String edad;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "email")
    private String email;

    @Column(name = "numero")
    private String numero;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pacienteHora")
    @JsonIgnoreProperties(value = { "pacienteHora", "horasMedicas", "horasCentroSalud" }, allowSetters = true)
    private Set<AgendaMedica> pacientes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Paciente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Paciente nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Paciente apellido(String apellido) {
        this.setApellido(apellido);
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getFechanacimiento() {
        return this.fechanacimiento;
    }

    public Paciente fechanacimiento(String fechanacimiento) {
        this.setFechanacimiento(fechanacimiento);
        return this;
    }

    public void setFechanacimiento(String fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public String getEdad() {
        return this.edad;
    }

    public Paciente edad(String edad) {
        this.setEdad(edad);
        return this;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Paciente direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return this.email;
    }

    public Paciente email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumero() {
        return this.numero;
    }

    public Paciente numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Set<AgendaMedica> getPacientes() {
        return this.pacientes;
    }

    public void setPacientes(Set<AgendaMedica> agendaMedicas) {
        if (this.pacientes != null) {
            this.pacientes.forEach(i -> i.setPacienteHora(null));
        }
        if (agendaMedicas != null) {
            agendaMedicas.forEach(i -> i.setPacienteHora(this));
        }
        this.pacientes = agendaMedicas;
    }

    public Paciente pacientes(Set<AgendaMedica> agendaMedicas) {
        this.setPacientes(agendaMedicas);
        return this;
    }

    public Paciente addPacientes(AgendaMedica agendaMedica) {
        this.pacientes.add(agendaMedica);
        agendaMedica.setPacienteHora(this);
        return this;
    }

    public Paciente removePacientes(AgendaMedica agendaMedica) {
        this.pacientes.remove(agendaMedica);
        agendaMedica.setPacienteHora(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paciente)) {
            return false;
        }
        return getId() != null && getId().equals(((Paciente) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Paciente{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", fechanacimiento='" + getFechanacimiento() + "'" +
            ", edad='" + getEdad() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", email='" + getEmail() + "'" +
            ", numero='" + getNumero() + "'" +
            "}";
    }
}
