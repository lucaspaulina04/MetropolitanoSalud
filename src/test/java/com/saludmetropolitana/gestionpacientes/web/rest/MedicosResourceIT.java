package com.saludmetropolitana.gestionpacientes.web.rest;

import static com.saludmetropolitana.gestionpacientes.domain.MedicosAsserts.*;
import static com.saludmetropolitana.gestionpacientes.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saludmetropolitana.gestionpacientes.IntegrationTest;
import com.saludmetropolitana.gestionpacientes.domain.Medicos;
import com.saludmetropolitana.gestionpacientes.repository.MedicosRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MedicosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MedicosResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_ESPECIALIDAD = "AAAAAAAAAA";
    private static final String UPDATED_ESPECIALIDAD = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/medicos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicosRepository medicosRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicosMockMvc;

    private Medicos medicos;

    private Medicos insertedMedicos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicos createEntity() {
        return new Medicos()
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .especialidad(DEFAULT_ESPECIALIDAD)
            .email(DEFAULT_EMAIL)
            .telefono(DEFAULT_TELEFONO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicos createUpdatedEntity() {
        return new Medicos()
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .especialidad(UPDATED_ESPECIALIDAD)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO);
    }

    @BeforeEach
    public void initTest() {
        medicos = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMedicos != null) {
            medicosRepository.delete(insertedMedicos);
            insertedMedicos = null;
        }
    }

    @Test
    @Transactional
    void createMedicos() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Medicos
        var returnedMedicos = om.readValue(
            restMedicosMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicos)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Medicos.class
        );

        // Validate the Medicos in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMedicosUpdatableFieldsEquals(returnedMedicos, getPersistedMedicos(returnedMedicos));

        insertedMedicos = returnedMedicos;
    }

    @Test
    @Transactional
    void createMedicosWithExistingId() throws Exception {
        // Create the Medicos with an existing ID
        medicos.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicos)))
            .andExpect(status().isBadRequest());

        // Validate the Medicos in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMedicos() throws Exception {
        // Initialize the database
        insertedMedicos = medicosRepository.saveAndFlush(medicos);

        // Get all the medicosList
        restMedicosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicos.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].especialidad").value(hasItem(DEFAULT_ESPECIALIDAD)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)));
    }

    @Test
    @Transactional
    void getMedicos() throws Exception {
        // Initialize the database
        insertedMedicos = medicosRepository.saveAndFlush(medicos);

        // Get the medicos
        restMedicosMockMvc
            .perform(get(ENTITY_API_URL_ID, medicos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicos.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.especialidad").value(DEFAULT_ESPECIALIDAD))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO));
    }

    @Test
    @Transactional
    void getNonExistingMedicos() throws Exception {
        // Get the medicos
        restMedicosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedicos() throws Exception {
        // Initialize the database
        insertedMedicos = medicosRepository.saveAndFlush(medicos);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicos
        Medicos updatedMedicos = medicosRepository.findById(medicos.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedicos are not directly saved in db
        em.detach(updatedMedicos);
        updatedMedicos
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .especialidad(UPDATED_ESPECIALIDAD)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO);

        restMedicosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMedicos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMedicos))
            )
            .andExpect(status().isOk());

        // Validate the Medicos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicosToMatchAllProperties(updatedMedicos);
    }

    @Test
    @Transactional
    void putNonExistingMedicos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicos.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicosMockMvc
            .perform(put(ENTITY_API_URL_ID, medicos.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicos)))
            .andExpect(status().isBadRequest());

        // Validate the Medicos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medicos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicosWithPatch() throws Exception {
        // Initialize the database
        insertedMedicos = medicosRepository.saveAndFlush(medicos);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicos using partial update
        Medicos partialUpdatedMedicos = new Medicos();
        partialUpdatedMedicos.setId(medicos.getId());

        partialUpdatedMedicos.apellido(UPDATED_APELLIDO).especialidad(UPDATED_ESPECIALIDAD).email(UPDATED_EMAIL);

        restMedicosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicos.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicos))
            )
            .andExpect(status().isOk());

        // Validate the Medicos in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicosUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMedicos, medicos), getPersistedMedicos(medicos));
    }

    @Test
    @Transactional
    void fullUpdateMedicosWithPatch() throws Exception {
        // Initialize the database
        insertedMedicos = medicosRepository.saveAndFlush(medicos);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicos using partial update
        Medicos partialUpdatedMedicos = new Medicos();
        partialUpdatedMedicos.setId(medicos.getId());

        partialUpdatedMedicos
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .especialidad(UPDATED_ESPECIALIDAD)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO);

        restMedicosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicos.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicos))
            )
            .andExpect(status().isOk());

        // Validate the Medicos in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicosUpdatableFieldsEquals(partialUpdatedMedicos, getPersistedMedicos(partialUpdatedMedicos));
    }

    @Test
    @Transactional
    void patchNonExistingMedicos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicos.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicos.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicosMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medicos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicos() throws Exception {
        // Initialize the database
        insertedMedicos = medicosRepository.saveAndFlush(medicos);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medicos
        restMedicosMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicosRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Medicos getPersistedMedicos(Medicos medicos) {
        return medicosRepository.findById(medicos.getId()).orElseThrow();
    }

    protected void assertPersistedMedicosToMatchAllProperties(Medicos expectedMedicos) {
        assertMedicosAllPropertiesEquals(expectedMedicos, getPersistedMedicos(expectedMedicos));
    }

    protected void assertPersistedMedicosToMatchUpdatableProperties(Medicos expectedMedicos) {
        assertMedicosAllUpdatablePropertiesEquals(expectedMedicos, getPersistedMedicos(expectedMedicos));
    }
}
