package com.saludmetropolitana.gestionpacientes.repository;

import com.saludmetropolitana.gestionpacientes.domain.AgendaMedica;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AgendaMedica entity.
 */
@Repository
public interface AgendaMedicaRepository extends JpaRepository<AgendaMedica, Long> {
    default Optional<AgendaMedica> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AgendaMedica> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AgendaMedica> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select agendaMedica from AgendaMedica agendaMedica left join fetch agendaMedica.pacienteHora left join fetch agendaMedica.horasMedicas left join fetch agendaMedica.horasCentroSalud",
        countQuery = "select count(agendaMedica) from AgendaMedica agendaMedica"
    )
    Page<AgendaMedica> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select agendaMedica from AgendaMedica agendaMedica left join fetch agendaMedica.pacienteHora left join fetch agendaMedica.horasMedicas left join fetch agendaMedica.horasCentroSalud"
    )
    List<AgendaMedica> findAllWithToOneRelationships();

    @Query(
        "select agendaMedica from AgendaMedica agendaMedica left join fetch agendaMedica.pacienteHora left join fetch agendaMedica.horasMedicas left join fetch agendaMedica.horasCentroSalud where agendaMedica.id =:id"
    )
    Optional<AgendaMedica> findOneWithToOneRelationships(@Param("id") Long id);
}
