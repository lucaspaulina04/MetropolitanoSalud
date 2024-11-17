package com.saludmetropolitana.gestionpacientes.repository;

import com.saludmetropolitana.gestionpacientes.domain.Medicos;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Medicos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicosRepository extends JpaRepository<Medicos, Long> {}
