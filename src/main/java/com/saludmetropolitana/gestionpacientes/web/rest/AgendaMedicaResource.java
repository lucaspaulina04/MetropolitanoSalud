package com.saludmetropolitana.gestionpacientes.web.rest;

import com.saludmetropolitana.gestionpacientes.domain.AgendaMedica;
import com.saludmetropolitana.gestionpacientes.repository.AgendaMedicaRepository;
import com.saludmetropolitana.gestionpacientes.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.saludmetropolitana.gestionpacientes.domain.AgendaMedica}.
 */
@RestController
@RequestMapping("/api/agenda-medicas")
@Transactional
public class AgendaMedicaResource {

    private static final Logger LOG = LoggerFactory.getLogger(AgendaMedicaResource.class);

    private static final String ENTITY_NAME = "agendaMedica";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgendaMedicaRepository agendaMedicaRepository;

    public AgendaMedicaResource(AgendaMedicaRepository agendaMedicaRepository) {
        this.agendaMedicaRepository = agendaMedicaRepository;
    }

    /**
     * {@code POST  /agenda-medicas} : Create a new agendaMedica.
     *
     * @param agendaMedica the agendaMedica to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agendaMedica, or with status {@code 400 (Bad Request)} if the agendaMedica has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AgendaMedica> createAgendaMedica(@RequestBody AgendaMedica agendaMedica) throws URISyntaxException {
        LOG.debug("REST request to save AgendaMedica : {}", agendaMedica);
        if (agendaMedica.getId() != null) {
            throw new BadRequestAlertException("A new agendaMedica cannot already have an ID", ENTITY_NAME, "idexists");
        }
        agendaMedica = agendaMedicaRepository.save(agendaMedica);
        return ResponseEntity.created(new URI("/api/agenda-medicas/" + agendaMedica.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, agendaMedica.getId().toString()))
            .body(agendaMedica);
    }

    /**
     * {@code PUT  /agenda-medicas/:id} : Updates an existing agendaMedica.
     *
     * @param id the id of the agendaMedica to save.
     * @param agendaMedica the agendaMedica to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agendaMedica,
     * or with status {@code 400 (Bad Request)} if the agendaMedica is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agendaMedica couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AgendaMedica> updateAgendaMedica(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AgendaMedica agendaMedica
    ) throws URISyntaxException {
        LOG.debug("REST request to update AgendaMedica : {}, {}", id, agendaMedica);
        if (agendaMedica.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agendaMedica.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agendaMedicaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        agendaMedica = agendaMedicaRepository.save(agendaMedica);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, agendaMedica.getId().toString()))
            .body(agendaMedica);
    }

    /**
     * {@code PATCH  /agenda-medicas/:id} : Partial updates given fields of an existing agendaMedica, field will ignore if it is null
     *
     * @param id the id of the agendaMedica to save.
     * @param agendaMedica the agendaMedica to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agendaMedica,
     * or with status {@code 400 (Bad Request)} if the agendaMedica is not valid,
     * or with status {@code 404 (Not Found)} if the agendaMedica is not found,
     * or with status {@code 500 (Internal Server Error)} if the agendaMedica couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AgendaMedica> partialUpdateAgendaMedica(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AgendaMedica agendaMedica
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AgendaMedica partially : {}, {}", id, agendaMedica);
        if (agendaMedica.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agendaMedica.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agendaMedicaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AgendaMedica> result = agendaMedicaRepository
            .findById(agendaMedica.getId())
            .map(existingAgendaMedica -> {
                if (agendaMedica.getFechaHora() != null) {
                    existingAgendaMedica.setFechaHora(agendaMedica.getFechaHora());
                }
                if (agendaMedica.getPacienteID() != null) {
                    existingAgendaMedica.setPacienteID(agendaMedica.getPacienteID());
                }
                if (agendaMedica.getMedicoID() != null) {
                    existingAgendaMedica.setMedicoID(agendaMedica.getMedicoID());
                }
                if (agendaMedica.getCentroSaludID() != null) {
                    existingAgendaMedica.setCentroSaludID(agendaMedica.getCentroSaludID());
                }
                if (agendaMedica.getEstadoHora() != null) {
                    existingAgendaMedica.setEstadoHora(agendaMedica.getEstadoHora());
                }

                return existingAgendaMedica;
            })
            .map(agendaMedicaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, agendaMedica.getId().toString())
        );
    }

    /**
     * {@code GET  /agenda-medicas} : get all the agendaMedicas.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agendaMedicas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AgendaMedica>> getAllAgendaMedicas(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of AgendaMedicas");
        Page<AgendaMedica> page;
        if (eagerload) {
            page = agendaMedicaRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = agendaMedicaRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /agenda-medicas/:id} : get the "id" agendaMedica.
     *
     * @param id the id of the agendaMedica to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agendaMedica, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AgendaMedica> getAgendaMedica(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AgendaMedica : {}", id);
        Optional<AgendaMedica> agendaMedica = agendaMedicaRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(agendaMedica);
    }

    /**
     * {@code DELETE  /agenda-medicas/:id} : delete the "id" agendaMedica.
     *
     * @param id the id of the agendaMedica to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgendaMedica(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AgendaMedica : {}", id);
        agendaMedicaRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
