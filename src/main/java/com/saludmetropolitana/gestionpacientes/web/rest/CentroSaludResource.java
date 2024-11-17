package com.saludmetropolitana.gestionpacientes.web.rest;

import com.saludmetropolitana.gestionpacientes.domain.CentroSalud;
import com.saludmetropolitana.gestionpacientes.repository.CentroSaludRepository;
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
 * REST controller for managing {@link com.saludmetropolitana.gestionpacientes.domain.CentroSalud}.
 */
@RestController
@RequestMapping("/api/centro-saluds")
@Transactional
public class CentroSaludResource {

    private static final Logger LOG = LoggerFactory.getLogger(CentroSaludResource.class);

    private static final String ENTITY_NAME = "centroSalud";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CentroSaludRepository centroSaludRepository;

    public CentroSaludResource(CentroSaludRepository centroSaludRepository) {
        this.centroSaludRepository = centroSaludRepository;
    }

    /**
     * {@code POST  /centro-saluds} : Create a new centroSalud.
     *
     * @param centroSalud the centroSalud to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new centroSalud, or with status {@code 400 (Bad Request)} if the centroSalud has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CentroSalud> createCentroSalud(@RequestBody CentroSalud centroSalud) throws URISyntaxException {
        LOG.debug("REST request to save CentroSalud : {}", centroSalud);
        if (centroSalud.getId() != null) {
            throw new BadRequestAlertException("A new centroSalud cannot already have an ID", ENTITY_NAME, "idexists");
        }
        centroSalud = centroSaludRepository.save(centroSalud);
        return ResponseEntity.created(new URI("/api/centro-saluds/" + centroSalud.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, centroSalud.getId().toString()))
            .body(centroSalud);
    }

    /**
     * {@code PUT  /centro-saluds/:id} : Updates an existing centroSalud.
     *
     * @param id the id of the centroSalud to save.
     * @param centroSalud the centroSalud to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated centroSalud,
     * or with status {@code 400 (Bad Request)} if the centroSalud is not valid,
     * or with status {@code 500 (Internal Server Error)} if the centroSalud couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CentroSalud> updateCentroSalud(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CentroSalud centroSalud
    ) throws URISyntaxException {
        LOG.debug("REST request to update CentroSalud : {}, {}", id, centroSalud);
        if (centroSalud.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, centroSalud.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!centroSaludRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        centroSalud = centroSaludRepository.save(centroSalud);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, centroSalud.getId().toString()))
            .body(centroSalud);
    }

    /**
     * {@code PATCH  /centro-saluds/:id} : Partial updates given fields of an existing centroSalud, field will ignore if it is null
     *
     * @param id the id of the centroSalud to save.
     * @param centroSalud the centroSalud to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated centroSalud,
     * or with status {@code 400 (Bad Request)} if the centroSalud is not valid,
     * or with status {@code 404 (Not Found)} if the centroSalud is not found,
     * or with status {@code 500 (Internal Server Error)} if the centroSalud couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CentroSalud> partialUpdateCentroSalud(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CentroSalud centroSalud
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CentroSalud partially : {}, {}", id, centroSalud);
        if (centroSalud.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, centroSalud.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!centroSaludRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CentroSalud> result = centroSaludRepository
            .findById(centroSalud.getId())
            .map(existingCentroSalud -> {
                if (centroSalud.getNombre() != null) {
                    existingCentroSalud.setNombre(centroSalud.getNombre());
                }
                if (centroSalud.getDireccion() != null) {
                    existingCentroSalud.setDireccion(centroSalud.getDireccion());
                }
                if (centroSalud.getTelefono() != null) {
                    existingCentroSalud.setTelefono(centroSalud.getTelefono());
                }
                if (centroSalud.getTipo() != null) {
                    existingCentroSalud.setTipo(centroSalud.getTipo());
                }

                return existingCentroSalud;
            })
            .map(centroSaludRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, centroSalud.getId().toString())
        );
    }

    /**
     * {@code GET  /centro-saluds} : get all the centroSaluds.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of centroSaluds in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CentroSalud>> getAllCentroSaluds(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of CentroSaluds");
        Page<CentroSalud> page = centroSaludRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /centro-saluds/:id} : get the "id" centroSalud.
     *
     * @param id the id of the centroSalud to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the centroSalud, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CentroSalud> getCentroSalud(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CentroSalud : {}", id);
        Optional<CentroSalud> centroSalud = centroSaludRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(centroSalud);
    }

    /**
     * {@code DELETE  /centro-saluds/:id} : delete the "id" centroSalud.
     *
     * @param id the id of the centroSalud to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCentroSalud(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CentroSalud : {}", id);
        centroSaludRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
