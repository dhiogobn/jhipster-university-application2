package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.Teacher;
import com.myapp.repository.TeacherRepository;
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
 * Integration tests for the {@link TeacherResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TeacherResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_SPECIALIZATION = "AAAAAAAAAA";
    private static final String UPDATED_SPECIALIZATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/teachers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TeacherRepository teacherRepository;

    @Mock
    private TeacherRepository teacherRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTeacherMockMvc;

    private Teacher teacher;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Teacher createEntity(EntityManager em) {
        Teacher teacher = new Teacher()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .phone(DEFAULT_PHONE)
            .specialization(DEFAULT_SPECIALIZATION);
        return teacher;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Teacher createUpdatedEntity(EntityManager em) {
        Teacher teacher = new Teacher()
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .specialization(UPDATED_SPECIALIZATION);
        return teacher;
    }

    @BeforeEach
    public void initTest() {
        teacher = createEntity(em);
    }

    @Test
    @Transactional
    void createTeacher() throws Exception {
        int databaseSizeBeforeCreate = teacherRepository.findAll().size();
        // Create the Teacher
        restTeacherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teacher)))
            .andExpect(status().isCreated());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeCreate + 1);
        Teacher testTeacher = teacherList.get(teacherList.size() - 1);
        assertThat(testTeacher.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTeacher.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testTeacher.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testTeacher.getSpecialization()).isEqualTo(DEFAULT_SPECIALIZATION);
    }

    @Test
    @Transactional
    void createTeacherWithExistingId() throws Exception {
        // Create the Teacher with an existing ID
        teacher.setId(1L);

        int databaseSizeBeforeCreate = teacherRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeacherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teacher)))
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTeachers() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList
        restTeacherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teacher.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].specialization").value(hasItem(DEFAULT_SPECIALIZATION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTeachersWithEagerRelationshipsIsEnabled() throws Exception {
        when(teacherRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTeacherMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(teacherRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTeachersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(teacherRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTeacherMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(teacherRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTeacher() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get the teacher
        restTeacherMockMvc
            .perform(get(ENTITY_API_URL_ID, teacher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(teacher.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.specialization").value(DEFAULT_SPECIALIZATION));
    }

    @Test
    @Transactional
    void getNonExistingTeacher() throws Exception {
        // Get the teacher
        restTeacherMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTeacher() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();

        // Update the teacher
        Teacher updatedTeacher = teacherRepository.findById(teacher.getId()).get();
        // Disconnect from session so that the updates on updatedTeacher are not directly saved in db
        em.detach(updatedTeacher);
        updatedTeacher.name(UPDATED_NAME).address(UPDATED_ADDRESS).phone(UPDATED_PHONE).specialization(UPDATED_SPECIALIZATION);

        restTeacherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTeacher.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTeacher))
            )
            .andExpect(status().isOk());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
        Teacher testTeacher = teacherList.get(teacherList.size() - 1);
        assertThat(testTeacher.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTeacher.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testTeacher.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testTeacher.getSpecialization()).isEqualTo(UPDATED_SPECIALIZATION);
    }

    @Test
    @Transactional
    void putNonExistingTeacher() throws Exception {
        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();
        teacher.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, teacher.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teacher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTeacher() throws Exception {
        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();
        teacher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teacher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTeacher() throws Exception {
        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();
        teacher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teacher)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTeacherWithPatch() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();

        // Update the teacher using partial update
        Teacher partialUpdatedTeacher = new Teacher();
        partialUpdatedTeacher.setId(teacher.getId());

        partialUpdatedTeacher.name(UPDATED_NAME).phone(UPDATED_PHONE).specialization(UPDATED_SPECIALIZATION);

        restTeacherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeacher.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTeacher))
            )
            .andExpect(status().isOk());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
        Teacher testTeacher = teacherList.get(teacherList.size() - 1);
        assertThat(testTeacher.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTeacher.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testTeacher.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testTeacher.getSpecialization()).isEqualTo(UPDATED_SPECIALIZATION);
    }

    @Test
    @Transactional
    void fullUpdateTeacherWithPatch() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();

        // Update the teacher using partial update
        Teacher partialUpdatedTeacher = new Teacher();
        partialUpdatedTeacher.setId(teacher.getId());

        partialUpdatedTeacher.name(UPDATED_NAME).address(UPDATED_ADDRESS).phone(UPDATED_PHONE).specialization(UPDATED_SPECIALIZATION);

        restTeacherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeacher.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTeacher))
            )
            .andExpect(status().isOk());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
        Teacher testTeacher = teacherList.get(teacherList.size() - 1);
        assertThat(testTeacher.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTeacher.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testTeacher.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testTeacher.getSpecialization()).isEqualTo(UPDATED_SPECIALIZATION);
    }

    @Test
    @Transactional
    void patchNonExistingTeacher() throws Exception {
        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();
        teacher.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, teacher.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(teacher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTeacher() throws Exception {
        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();
        teacher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(teacher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTeacher() throws Exception {
        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();
        teacher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(teacher)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTeacher() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        int databaseSizeBeforeDelete = teacherRepository.findAll().size();

        // Delete the teacher
        restTeacherMockMvc
            .perform(delete(ENTITY_API_URL_ID, teacher.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
