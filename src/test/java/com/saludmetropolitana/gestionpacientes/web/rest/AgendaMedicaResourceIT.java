package com.saludmetropolitana.gestionpacientes.web.rest;

import static com.saludmetropolitana.gestionpacientes.domain.AgendaMedicaAsserts.*;
import static com.saludmetropolitana.gestionpacientes.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saludmetropolitana.gestionpacientes.IntegrationTest;
import com.saludmetropolitana.gestionpacientes.domain.AgendaMedica;
import com.saludmetropolitana.gestionpacientes.repository.AgendaMedicaRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AgendaMedicaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AgendaMedicaResourceIT {

    private static final String DEFAULT_FECHA_HORA = "AAAAAAAAAA";
    private static final String UPDATED_FECHA_HORA = "BBBBBBBBBB";

    private static final String DEFAULT_PACIENTE_ID = "AAAAAAAAAA";
    private static final String UPDATED_PACIENTE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_MEDICO_ID = "AAAAAAAAAA";
    private static final String UPDATED_MEDICO_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CENTRO_SALUD_ID = "AAAAAAAAAA";
    private static final String UPDATED_CENTRO_SALUD_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO_HORA = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO_HORA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/agenda-medicas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AgendaMedicaRepository agendaMedicaRepository;

    @Mock
    private AgendaMedicaRepository agendaMedicaRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgendaMedicaMockMvc;

    private AgendaMedica agendaMedica;

    private AgendaMedica insertedAgendaMedica;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgendaMedica createEntity() {
        return new AgendaMedica()
            .fechaHora(DEFAULT_FECHA_HORA)
            .pacienteID(DEFAULT_PACIENTE_ID)
            .medicoID(DEFAULT_MEDICO_ID)
            .centroSaludID(DEFAULT_CENTRO_SALUD_ID)
            .estadoHora(DEFAULT_ESTADO_HORA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgendaMedica createUpdatedEntity() {
        return new AgendaMedica()
            .fechaHora(UPDATED_FECHA_HORA)
            .pacienteID(UPDATED_PACIENTE_ID)
            .medicoID(UPDATED_MEDICO_ID)
            .centroSaludID(UPDATED_CENTRO_SALUD_ID)
            .estadoHora(UPDATED_ESTADO_HORA);
    }

    @BeforeEach
    public void initTest() {
        agendaMedica = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAgendaMedica != null) {
            agendaMedicaRepository.delete(insertedAgendaMedica);
            insertedAgendaMedica = null;
        }
    }

    @Test
    @Transactional
    void createAgendaMedica() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AgendaMedica
        var returnedAgendaMedica = om.readValue(
            restAgendaMedicaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agendaMedica)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AgendaMedica.class
        );

        // Validate the AgendaMedica in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAgendaMedicaUpdatableFieldsEquals(returnedAgendaMedica, getPersistedAgendaMedica(returnedAgendaMedica));

        insertedAgendaMedica = returnedAgendaMedica;
    }

    @Test
    @Transactional
    void createAgendaMedicaWithExistingId() throws Exception {
        // Create the AgendaMedica with an existing ID
        agendaMedica.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgendaMedicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agendaMedica)))
            .andExpect(status().isBadRequest());

        // Validate the AgendaMedica in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAgendaMedicas() throws Exception {
        // Initialize the database
        insertedAgendaMedica = agendaMedicaRepository.saveAndFlush(agendaMedica);

        // Get all the agendaMedicaList
        restAgendaMedicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agendaMedica.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaHora").value(hasItem(DEFAULT_FECHA_HORA)))
            .andExpect(jsonPath("$.[*].pacienteID").value(hasItem(DEFAULT_PACIENTE_ID)))
            .andExpect(jsonPath("$.[*].medicoID").value(hasItem(DEFAULT_MEDICO_ID)))
            .andExpect(jsonPath("$.[*].centroSaludID").value(hasItem(DEFAULT_CENTRO_SALUD_ID)))
            .andExpect(jsonPath("$.[*].estadoHora").value(hasItem(DEFAULT_ESTADO_HORA)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAgendaMedicasWithEagerRelationshipsIsEnabled() throws Exception {
        when(agendaMedicaRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAgendaMedicaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(agendaMedicaRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAgendaMedicasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(agendaMedicaRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAgendaMedicaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(agendaMedicaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAgendaMedica() throws Exception {
        // Initialize the database
        insertedAgendaMedica = agendaMedicaRepository.saveAndFlush(agendaMedica);

        // Get the agendaMedica
        restAgendaMedicaMockMvc
            .perform(get(ENTITY_API_URL_ID, agendaMedica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agendaMedica.getId().intValue()))
            .andExpect(jsonPath("$.fechaHora").value(DEFAULT_FECHA_HORA))
            .andExpect(jsonPath("$.pacienteID").value(DEFAULT_PACIENTE_ID))
            .andExpect(jsonPath("$.medicoID").value(DEFAULT_MEDICO_ID))
            .andExpect(jsonPath("$.centroSaludID").value(DEFAULT_CENTRO_SALUD_ID))
            .andExpect(jsonPath("$.estadoHora").value(DEFAULT_ESTADO_HORA));
    }

    @Test
    @Transactional
    void getNonExistingAgendaMedica() throws Exception {
        // Get the agendaMedica
        restAgendaMedicaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAgendaMedica() throws Exception {
        // Initialize the database
        insertedAgendaMedica = agendaMedicaRepository.saveAndFlush(agendaMedica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agendaMedica
        AgendaMedica updatedAgendaMedica = agendaMedicaRepository.findById(agendaMedica.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAgendaMedica are not directly saved in db
        em.detach(updatedAgendaMedica);
        updatedAgendaMedica
            .fechaHora(UPDATED_FECHA_HORA)
            .pacienteID(UPDATED_PACIENTE_ID)
            .medicoID(UPDATED_MEDICO_ID)
            .centroSaludID(UPDATED_CENTRO_SALUD_ID)
            .estadoHora(UPDATED_ESTADO_HORA);

        restAgendaMedicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAgendaMedica.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAgendaMedica))
            )
            .andExpect(status().isOk());

        // Validate the AgendaMedica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAgendaMedicaToMatchAllProperties(updatedAgendaMedica);
    }

    @Test
    @Transactional
    void putNonExistingAgendaMedica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agendaMedica.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgendaMedicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agendaMedica.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agendaMedica))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgendaMedica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgendaMedica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agendaMedica.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaMedicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agendaMedica))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgendaMedica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgendaMedica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agendaMedica.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaMedicaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agendaMedica)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgendaMedica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAgendaMedicaWithPatch() throws Exception {
        // Initialize the database
        insertedAgendaMedica = agendaMedicaRepository.saveAndFlush(agendaMedica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agendaMedica using partial update
        AgendaMedica partialUpdatedAgendaMedica = new AgendaMedica();
        partialUpdatedAgendaMedica.setId(agendaMedica.getId());

        partialUpdatedAgendaMedica
            .fechaHora(UPDATED_FECHA_HORA)
            .pacienteID(UPDATED_PACIENTE_ID)
            .medicoID(UPDATED_MEDICO_ID)
            .centroSaludID(UPDATED_CENTRO_SALUD_ID)
            .estadoHora(UPDATED_ESTADO_HORA);

        restAgendaMedicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgendaMedica.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgendaMedica))
            )
            .andExpect(status().isOk());

        // Validate the AgendaMedica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgendaMedicaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAgendaMedica, agendaMedica),
            getPersistedAgendaMedica(agendaMedica)
        );
    }

    @Test
    @Transactional
    void fullUpdateAgendaMedicaWithPatch() throws Exception {
        // Initialize the database
        insertedAgendaMedica = agendaMedicaRepository.saveAndFlush(agendaMedica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agendaMedica using partial update
        AgendaMedica partialUpdatedAgendaMedica = new AgendaMedica();
        partialUpdatedAgendaMedica.setId(agendaMedica.getId());

        partialUpdatedAgendaMedica
            .fechaHora(UPDATED_FECHA_HORA)
            .pacienteID(UPDATED_PACIENTE_ID)
            .medicoID(UPDATED_MEDICO_ID)
            .centroSaludID(UPDATED_CENTRO_SALUD_ID)
            .estadoHora(UPDATED_ESTADO_HORA);

        restAgendaMedicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgendaMedica.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgendaMedica))
            )
            .andExpect(status().isOk());

        // Validate the AgendaMedica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgendaMedicaUpdatableFieldsEquals(partialUpdatedAgendaMedica, getPersistedAgendaMedica(partialUpdatedAgendaMedica));
    }

    @Test
    @Transactional
    void patchNonExistingAgendaMedica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agendaMedica.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgendaMedicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agendaMedica.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agendaMedica))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgendaMedica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgendaMedica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agendaMedica.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaMedicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agendaMedica))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgendaMedica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgendaMedica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agendaMedica.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaMedicaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(agendaMedica)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgendaMedica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAgendaMedica() throws Exception {
        // Initialize the database
        insertedAgendaMedica = agendaMedicaRepository.saveAndFlush(agendaMedica);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the agendaMedica
        restAgendaMedicaMockMvc
            .perform(delete(ENTITY_API_URL_ID, agendaMedica.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return agendaMedicaRepository.count();
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

    protected AgendaMedica getPersistedAgendaMedica(AgendaMedica agendaMedica) {
        return agendaMedicaRepository.findById(agendaMedica.getId()).orElseThrow();
    }

    protected void assertPersistedAgendaMedicaToMatchAllProperties(AgendaMedica expectedAgendaMedica) {
        assertAgendaMedicaAllPropertiesEquals(expectedAgendaMedica, getPersistedAgendaMedica(expectedAgendaMedica));
    }

    protected void assertPersistedAgendaMedicaToMatchUpdatableProperties(AgendaMedica expectedAgendaMedica) {
        assertAgendaMedicaAllUpdatablePropertiesEquals(expectedAgendaMedica, getPersistedAgendaMedica(expectedAgendaMedica));
    }
}
