package com.saludmetropolitana.gestionpacientes.web.rest;

import static com.saludmetropolitana.gestionpacientes.domain.PacienteAsserts.*;
import static com.saludmetropolitana.gestionpacientes.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saludmetropolitana.gestionpacientes.IntegrationTest;
import com.saludmetropolitana.gestionpacientes.domain.Paciente;
import com.saludmetropolitana.gestionpacientes.repository.PacienteRepository;
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
 * Integration tests for the {@link PacienteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PacienteResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_FECHANACIMIENTO = "AAAAAAAAAA";
    private static final String UPDATED_FECHANACIMIENTO = "BBBBBBBBBB";

    private static final String DEFAULT_EDAD = "AAAAAAAAAA";
    private static final String UPDATED_EDAD = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pacientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPacienteMockMvc;

    private Paciente paciente;

    private Paciente insertedPaciente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createEntity() {
        return new Paciente()
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .fechanacimiento(DEFAULT_FECHANACIMIENTO)
            .edad(DEFAULT_EDAD)
            .direccion(DEFAULT_DIRECCION)
            .email(DEFAULT_EMAIL)
            .numero(DEFAULT_NUMERO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createUpdatedEntity() {
        return new Paciente()
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .fechanacimiento(UPDATED_FECHANACIMIENTO)
            .edad(UPDATED_EDAD)
            .direccion(UPDATED_DIRECCION)
            .email(UPDATED_EMAIL)
            .numero(UPDATED_NUMERO);
    }

    @BeforeEach
    public void initTest() {
        paciente = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPaciente != null) {
            pacienteRepository.delete(insertedPaciente);
            insertedPaciente = null;
        }
    }

    @Test
    @Transactional
    void createPaciente() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Paciente
        var returnedPaciente = om.readValue(
            restPacienteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paciente)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Paciente.class
        );

        // Validate the Paciente in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPacienteUpdatableFieldsEquals(returnedPaciente, getPersistedPaciente(returnedPaciente));

        insertedPaciente = returnedPaciente;
    }

    @Test
    @Transactional
    void createPacienteWithExistingId() throws Exception {
        // Create the Paciente with an existing ID
        paciente.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPacienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paciente)))
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPacientes() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].fechanacimiento").value(hasItem(DEFAULT_FECHANACIMIENTO)))
            .andExpect(jsonPath("$.[*].edad").value(hasItem(DEFAULT_EDAD)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)));
    }

    @Test
    @Transactional
    void getPaciente() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        // Get the paciente
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL_ID, paciente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paciente.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.fechanacimiento").value(DEFAULT_FECHANACIMIENTO))
            .andExpect(jsonPath("$.edad").value(DEFAULT_EDAD))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO));
    }

    @Test
    @Transactional
    void getNonExistingPaciente() throws Exception {
        // Get the paciente
        restPacienteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaciente() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paciente
        Paciente updatedPaciente = pacienteRepository.findById(paciente.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaciente are not directly saved in db
        em.detach(updatedPaciente);
        updatedPaciente
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .fechanacimiento(UPDATED_FECHANACIMIENTO)
            .edad(UPDATED_EDAD)
            .direccion(UPDATED_DIRECCION)
            .email(UPDATED_EMAIL)
            .numero(UPDATED_NUMERO);

        restPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaciente.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPaciente))
            )
            .andExpect(status().isOk());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPacienteToMatchAllProperties(updatedPaciente);
    }

    @Test
    @Transactional
    void putNonExistingPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paciente.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paciente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paciente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paciente)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePacienteWithPatch() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paciente using partial update
        Paciente partialUpdatedPaciente = new Paciente();
        partialUpdatedPaciente.setId(paciente.getId());

        partialUpdatedPaciente.numero(UPDATED_NUMERO);

        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaciente.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaciente))
            )
            .andExpect(status().isOk());

        // Validate the Paciente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPacienteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPaciente, paciente), getPersistedPaciente(paciente));
    }

    @Test
    @Transactional
    void fullUpdatePacienteWithPatch() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paciente using partial update
        Paciente partialUpdatedPaciente = new Paciente();
        partialUpdatedPaciente.setId(paciente.getId());

        partialUpdatedPaciente
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .fechanacimiento(UPDATED_FECHANACIMIENTO)
            .edad(UPDATED_EDAD)
            .direccion(UPDATED_DIRECCION)
            .email(UPDATED_EMAIL)
            .numero(UPDATED_NUMERO);

        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaciente.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaciente))
            )
            .andExpect(status().isOk());

        // Validate the Paciente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPacienteUpdatableFieldsEquals(partialUpdatedPaciente, getPersistedPaciente(partialUpdatedPaciente));
    }

    @Test
    @Transactional
    void patchNonExistingPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paciente.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paciente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paciente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paciente)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaciente() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.saveAndFlush(paciente);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paciente
        restPacienteMockMvc
            .perform(delete(ENTITY_API_URL_ID, paciente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pacienteRepository.count();
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

    protected Paciente getPersistedPaciente(Paciente paciente) {
        return pacienteRepository.findById(paciente.getId()).orElseThrow();
    }

    protected void assertPersistedPacienteToMatchAllProperties(Paciente expectedPaciente) {
        assertPacienteAllPropertiesEquals(expectedPaciente, getPersistedPaciente(expectedPaciente));
    }

    protected void assertPersistedPacienteToMatchUpdatableProperties(Paciente expectedPaciente) {
        assertPacienteAllUpdatablePropertiesEquals(expectedPaciente, getPersistedPaciente(expectedPaciente));
    }
}
