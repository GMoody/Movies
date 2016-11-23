package org.movies.gm.web.rest;

import org.movies.gm.MoviesApp;

import org.movies.gm.domain.Writer;
import org.movies.gm.repository.WriterRepository;
import org.movies.gm.service.WriterService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WriterResource REST controller.
 *
 * @see WriterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MoviesApp.class)
public class WriterResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    @Inject
    private WriterRepository writerRepository;

    @Inject
    private WriterService writerService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restWriterMockMvc;

    private Writer writer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WriterResource writerResource = new WriterResource();
        ReflectionTestUtils.setField(writerResource, "writerService", writerService);
        this.restWriterMockMvc = MockMvcBuilders.standaloneSetup(writerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Writer createEntity(EntityManager em) {
        Writer writer = new Writer()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME);
        return writer;
    }

    @Before
    public void initTest() {
        writer = createEntity(em);
    }

    @Test
    @Transactional
    public void createWriter() throws Exception {
        int databaseSizeBeforeCreate = writerRepository.findAll().size();

        // Create the Writer

        restWriterMockMvc.perform(post("/api/writers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(writer)))
                .andExpect(status().isCreated());

        // Validate the Writer in the database
        List<Writer> writers = writerRepository.findAll();
        assertThat(writers).hasSize(databaseSizeBeforeCreate + 1);
        Writer testWriter = writers.get(writers.size() - 1);
        assertThat(testWriter.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testWriter.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = writerRepository.findAll().size();
        // set the field null
        writer.setFirstName(null);

        // Create the Writer, which fails.

        restWriterMockMvc.perform(post("/api/writers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(writer)))
                .andExpect(status().isBadRequest());

        List<Writer> writers = writerRepository.findAll();
        assertThat(writers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = writerRepository.findAll().size();
        // set the field null
        writer.setLastName(null);

        // Create the Writer, which fails.

        restWriterMockMvc.perform(post("/api/writers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(writer)))
                .andExpect(status().isBadRequest());

        List<Writer> writers = writerRepository.findAll();
        assertThat(writers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWriters() throws Exception {
        // Initialize the database
        writerRepository.saveAndFlush(writer);

        // Get all the writers
        restWriterMockMvc.perform(get("/api/writers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(writer.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())));
    }

    @Test
    @Transactional
    public void getWriter() throws Exception {
        // Initialize the database
        writerRepository.saveAndFlush(writer);

        // Get the writer
        restWriterMockMvc.perform(get("/api/writers/{id}", writer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(writer.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWriter() throws Exception {
        // Get the writer
        restWriterMockMvc.perform(get("/api/writers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWriter() throws Exception {
        // Initialize the database
        writerService.save(writer);

        int databaseSizeBeforeUpdate = writerRepository.findAll().size();

        // Update the writer
        Writer updatedWriter = writerRepository.findOne(writer.getId());
        updatedWriter
                .firstName(UPDATED_FIRST_NAME)
                .lastName(UPDATED_LAST_NAME);

        restWriterMockMvc.perform(put("/api/writers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedWriter)))
                .andExpect(status().isOk());

        // Validate the Writer in the database
        List<Writer> writers = writerRepository.findAll();
        assertThat(writers).hasSize(databaseSizeBeforeUpdate);
        Writer testWriter = writers.get(writers.size() - 1);
        assertThat(testWriter.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testWriter.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void deleteWriter() throws Exception {
        // Initialize the database
        writerService.save(writer);

        int databaseSizeBeforeDelete = writerRepository.findAll().size();

        // Get the writer
        restWriterMockMvc.perform(delete("/api/writers/{id}", writer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Writer> writers = writerRepository.findAll();
        assertThat(writers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
