package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.Discipline;
import com.myapp.repository.DisciplineRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DisciplineResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DisciplineResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CREDITS = "AAAAAAAAAA";
    private static final String UPDATED_CREDITS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/disciplines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Mock
    private DisciplineRepository disciplineRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDisciplineMockMvc;

    private Discipline discipline;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Discipline createEntity(EntityManager em) {
        Discipline discipline = new Discipline().name(DEFAULT_NAME).credits(DEFAULT_CREDITS).phone(DEFAULT_PHONE);
        return discipline;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Discipline createUpdatedEntity(EntityManager em) {
        Discipline discipline = new Discipline().name(UPDATED_NAME).credits(UPDATED_CREDITS).phone(UPDATED_PHONE);
        return discipline;
    }

    @BeforeEach
    public void initTest() {
        discipline = createEntity(em);
    }

    @Test
    @Transactional
    void createDiscipline() throws Exception {
        int databaseSizeBeforeCreate = disciplineRepository.findAll().size();
        // Create the Discipline
        restDisciplineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(discipline)))
            .andExpect(status().isCreated());

        // Validate the Discipline in the database
        List<Discipline> disciplineList = disciplineRepository.findAll();
        assertThat(disciplineList).hasSize(databaseSizeBeforeCreate + 1);
        Discipline testDiscipline = disciplineList.get(disciplineList.size() - 1);
        assertThat(testDiscipline.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDiscipline.getCredits()).isEqualTo(DEFAULT_CREDITS);
        assertThat(testDiscipline.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void createDisciplineWithExistingId() throws Exception {
        // Create the Discipline with an existing ID
        discipline.setId(1L);

        int databaseSizeBeforeCreate = disciplineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDisciplineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(discipline)))
            .andExpect(status().isBadRequest());

        // Validate the Discipline in the database
        List<Discipline> disciplineList = disciplineRepository.findAll();
        assertThat(disciplineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDisciplines() throws Exception {
        // Initialize the database
        disciplineRepository.saveAndFlush(discipline);

        // Get all the disciplineList
        restDisciplineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discipline.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].credits").value(hasItem(DEFAULT_CREDITS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDisciplinesWithEagerRelationshipsIsEnabled() throws Exception {
        when(disciplineRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDisciplineMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(disciplineRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDisciplinesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(disciplineRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDisciplineMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(disciplineRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDiscipline() throws Exception {
        // Initialize the database
        disciplineRepository.saveAndFlush(discipline);

        // Get the discipline
        restDisciplineMockMvc
            .perform(get(ENTITY_API_URL_ID, discipline.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(discipline.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.credits").value(DEFAULT_CREDITS))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }

    @Test
    @Transactional
    void getNonExistingDiscipline() throws Exception {
        // Get the discipline
        restDisciplineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDiscipline() throws Exception {
        // Initialize the database
        disciplineRepository.saveAndFlush(discipline);

        int databaseSizeBeforeUpdate = disciplineRepository.findAll().size();

        // Update the discipline
        Discipline updatedDiscipline = disciplineRepository.findById(discipline.getId()).get();
        // Disconnect from session so that the updates on updatedDiscipline are not directly saved in db
        em.detach(updatedDiscipline);
        updatedDiscipline.name(UPDATED_NAME).credits(UPDATED_CREDITS).phone(UPDATED_PHONE);

        restDisciplineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDiscipline.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDiscipline))
            )
            .andExpect(status().isOk());

        // Validate the Discipline in the database
        List<Discipline> disciplineList = disciplineRepository.findAll();
        assertThat(disciplineList).hasSize(databaseSizeBeforeUpdate);
        Discipline testDiscipline = disciplineList.get(disciplineList.size() - 1);
        assertThat(testDiscipline.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDiscipline.getCredits()).isEqualTo(UPDATED_CREDITS);
        assertThat(testDiscipline.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void putNonExistingDiscipline() throws Exception {
        int databaseSizeBeforeUpdate = disciplineRepository.findAll().size();
        discipline.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisciplineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, discipline.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(discipline))
            )
            .andExpect(status().isBadRequest());

        // Validate the Discipline in the database
        List<Discipline> disciplineList = disciplineRepository.findAll();
        assertThat(disciplineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDiscipline() throws Exception {
        int databaseSizeBeforeUpdate = disciplineRepository.findAll().size();
        discipline.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(discipline))
            )
            .andExpect(status().isBadRequest());

        // Validate the Discipline in the database
        List<Discipline> disciplineList = disciplineRepository.findAll();
        assertThat(disciplineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDiscipline() throws Exception {
        int databaseSizeBeforeUpdate = disciplineRepository.findAll().size();
        discipline.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(discipline)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Discipline in the database
        List<Discipline> disciplineList = disciplineRepository.findAll();
        assertThat(disciplineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDisciplineWithPatch() throws Exception {
        // Initialize the database
        disciplineRepository.saveAndFlush(discipline);

        int databaseSizeBeforeUpdate = disciplineRepository.findAll().size();

        // Update the discipline using partial update
        Discipline partialUpdatedDiscipline = new Discipline();
        partialUpdatedDiscipline.setId(discipline.getId());

        partialUpdatedDiscipline.credits(UPDATED_CREDITS).phone(UPDATED_PHONE);

        restDisciplineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDiscipline.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDiscipline))
            )
            .andExpect(status().isOk());

        // Validate the Discipline in the database
        List<Discipline> disciplineList = disciplineRepository.findAll();
        assertThat(disciplineList).hasSize(databaseSizeBeforeUpdate);
        Discipline testDiscipline = disciplineList.get(disciplineList.size() - 1);
        assertThat(testDiscipline.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDiscipline.getCredits()).isEqualTo(UPDATED_CREDITS);
        assertThat(testDiscipline.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void fullUpdateDisciplineWithPatch() throws Exception {
        // Initialize the database
        disciplineRepository.saveAndFlush(discipline);

        int databaseSizeBeforeUpdate = disciplineRepository.findAll().size();

        // Update the discipline using partial update
        Discipline partialUpdatedDiscipline = new Discipline();
        partialUpdatedDiscipline.setId(discipline.getId());

        partialUpdatedDiscipline.name(UPDATED_NAME).credits(UPDATED_CREDITS).phone(UPDATED_PHONE);

        restDisciplineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDiscipline.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDiscipline))
            )
            .andExpect(status().isOk());

        // Validate the Discipline in the database
        List<Discipline> disciplineList = disciplineRepository.findAll();
        assertThat(disciplineList).hasSize(databaseSizeBeforeUpdate);
        Discipline testDiscipline = disciplineList.get(disciplineList.size() - 1);
        assertThat(testDiscipline.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDiscipline.getCredits()).isEqualTo(UPDATED_CREDITS);
        assertThat(testDiscipline.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void patchNonExistingDiscipline() throws Exception {
        int databaseSizeBeforeUpdate = disciplineRepository.findAll().size();
        discipline.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisciplineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, discipline.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(discipline))
            )
            .andExpect(status().isBadRequest());

        // Validate the Discipline in the database
        List<Discipline> disciplineList = disciplineRepository.findAll();
        assertThat(disciplineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDiscipline() throws Exception {
        int databaseSizeBeforeUpdate = disciplineRepository.findAll().size();
        discipline.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(discipline))
            )
            .andExpect(status().isBadRequest());

        // Validate the Discipline in the database
        List<Discipline> disciplineList = disciplineRepository.findAll();
        assertThat(disciplineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDiscipline() throws Exception {
        int databaseSizeBeforeUpdate = disciplineRepository.findAll().size();
        discipline.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplineMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(discipline))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Discipline in the database
        List<Discipline> disciplineList = disciplineRepository.findAll();
        assertThat(disciplineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDiscipline() throws Exception {
        // Initialize the database
        disciplineRepository.saveAndFlush(discipline);

        int databaseSizeBeforeDelete = disciplineRepository.findAll().size();

        // Delete the discipline
        restDisciplineMockMvc
            .perform(delete(ENTITY_API_URL_ID, discipline.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Discipline> disciplineList = disciplineRepository.findAll();
        assertThat(disciplineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
