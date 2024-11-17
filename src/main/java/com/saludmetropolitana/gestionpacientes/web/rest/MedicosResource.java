package com.saludmetropolitana.gestionpacientes.web.rest;

import com.saludmetropolitana.gestionpacientes.domain.Medicos;
import com.saludmetropolitana.gestionpacientes.repository.MedicosRepository;
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
 * REST controller for managing {@link com.saludmetropolitana.gestionpacientes.domain.Medicos}.
 */
@RestController
@RequestMapping("/api/medicos")
@Transactional
public class MedicosResource {

    private static final Logger LOG = LoggerFactory.getLogger(MedicosResource.class);

    private static final String ENTITY_NAME = "medicos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicosRepository medicosRepository;

    public MedicosResource(MedicosRepository medicosRepository) {
        this.medicosRepository = medicosRepository;
    }

    /**
     * {@code POST  /medicos} : Create a new medicos.
     *
     * @param medicos the medicos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicos, or with status {@code 400 (Bad Request)} if the medicos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Medicos> createMedicos(@RequestBody Medicos medicos) throws URISyntaxException {
        LOG.debug("REST request to save Medicos : {}", medicos);
        if (medicos.getId() != null) {
            throw new BadRequestAlertException("A new medicos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        medicos = medicosRepository.save(medicos);
        return ResponseEntity.created(new URI("/api/medicos/" + medicos.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, medicos.getId().toString()))
            .body(medicos);
    }

    /**
     * {@code PUT  /medicos/:id} : Updates an existing medicos.
     *
     * @param id the id of the medicos to save.
     * @param medicos the medicos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicos,
     * or with status {@code 400 (Bad Request)} if the medicos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Medicos> updateMedicos(@PathVariable(value = "id", required = false) final Long id, @RequestBody Medicos medicos)
        throws URISyntaxException {
        LOG.debug("REST request to update Medicos : {}, {}", id, medicos);
        if (medicos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        medicos = medicosRepository.save(medicos);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, medicos.getId().toString()))
            .body(medicos);
    }

    /**
     * {@code PATCH  /medicos/:id} : Partial updates given fields of an existing medicos, field will ignore if it is null
     *
     * @param id the id of the medicos to save.
     * @param medicos the medicos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicos,
     * or with status {@code 400 (Bad Request)} if the medicos is not valid,
     * or with status {@code 404 (Not Found)} if the medicos is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Medicos> partialUpdateMedicos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Medicos medicos
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Medicos partially : {}, {}", id, medicos);
        if (medicos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Medicos> result = medicosRepository
            .findById(medicos.getId())
            .map(existingMedicos -> {
                if (medicos.getNombre() != null) {
                    existingMedicos.setNombre(medicos.getNombre());
                }
                if (medicos.getApellido() != null) {
                    existingMedicos.setApellido(medicos.getApellido());
                }
                if (medicos.getEspecialidad() != null) {
                    existingMedicos.setEspecialidad(medicos.getEspecialidad());
                }
                if (medicos.getEmail() != null) {
                    existingMedicos.setEmail(medicos.getEmail());
                }
                if (medicos.getTelefono() != null) {
                    existingMedicos.setTelefono(medicos.getTelefono());
                }

                return existingMedicos;
            })
            .map(medicosRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, medicos.getId().toString())
        );
    }

    /**
     * {@code GET  /medicos} : get all the medicos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Medicos>> getAllMedicos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Medicos");
        Page<Medicos> page = medicosRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /medicos/:id} : get the "id" medicos.
     *
     * @param id the id of the medicos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Medicos> getMedicos(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Medicos : {}", id);
        Optional<Medicos> medicos = medicosRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(medicos);
    }

    /**
     * {@code DELETE  /medicos/:id} : delete the "id" medicos.
     *
     * @param id the id of the medicos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicos(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Medicos : {}", id);
        medicosRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
