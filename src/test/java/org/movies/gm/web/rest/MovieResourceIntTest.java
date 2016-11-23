package org.movies.gm.web.rest;

import org.movies.gm.MoviesApp;

import org.movies.gm.domain.Movie;
import org.movies.gm.domain.Director;
import org.movies.gm.domain.Writer;
import org.movies.gm.domain.Genre;
import org.movies.gm.domain.Actor;
import org.movies.gm.domain.Country;
import org.movies.gm.repository.MovieRepository;
import org.movies.gm.service.MovieService;

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
 * Test class for the MovieResource REST controller.
 *
 * @see MovieResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MoviesApp.class)
public class MovieResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEAR = 1890;
    private static final Integer UPDATED_YEAR = 1891;

    private static final Double DEFAULT_DURATION = 0.1D;
    private static final Double UPDATED_DURATION = 1D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE_RATING = 1;
    private static final Integer UPDATED_AGE_RATING = 2;

    @Inject
    private MovieRepository movieRepository;

    @Inject
    private MovieService movieService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMovieMockMvc;

    private Movie movie;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MovieResource movieResource = new MovieResource();
        ReflectionTestUtils.setField(movieResource, "movieService", movieService);
        this.restMovieMockMvc = MockMvcBuilders.standaloneSetup(movieResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Movie createEntity(EntityManager em) {
        Movie movie = new Movie()
                .title(DEFAULT_TITLE)
                .year(DEFAULT_YEAR)
                .duration(DEFAULT_DURATION)
                .description(DEFAULT_DESCRIPTION)
                .ageRating(DEFAULT_AGE_RATING);
        // Add required entity
        Director director = DirectorResourceIntTest.createEntity(em);
        em.persist(director);
        em.flush();
        movie.getDirectors().add(director);
        // Add required entity
        Writer writer = WriterResourceIntTest.createEntity(em);
        em.persist(writer);
        em.flush();
        movie.getWriters().add(writer);
        // Add required entity
        Genre genre = GenreResourceIntTest.createEntity(em);
        em.persist(genre);
        em.flush();
        movie.getGenres().add(genre);
        // Add required entity
        Actor actor = ActorResourceIntTest.createEntity(em);
        em.persist(actor);
        em.flush();
        movie.getActors().add(actor);
        // Add required entity
        Country country = CountryResourceIntTest.createEntity(em);
        em.persist(country);
        em.flush();
        movie.getCountries().add(country);
        return movie;
    }

    @Before
    public void initTest() {
        movie = createEntity(em);
    }

    @Test
    @Transactional
    public void createMovie() throws Exception {
        int databaseSizeBeforeCreate = movieRepository.findAll().size();

        // Create the Movie

        restMovieMockMvc.perform(post("/api/movies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(movie)))
                .andExpect(status().isCreated());

        // Validate the Movie in the database
        List<Movie> movies = movieRepository.findAll();
        assertThat(movies).hasSize(databaseSizeBeforeCreate + 1);
        Movie testMovie = movies.get(movies.size() - 1);
        assertThat(testMovie.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMovie.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testMovie.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testMovie.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMovie.getAgeRating()).isEqualTo(DEFAULT_AGE_RATING);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = movieRepository.findAll().size();
        // set the field null
        movie.setTitle(null);

        // Create the Movie, which fails.

        restMovieMockMvc.perform(post("/api/movies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(movie)))
                .andExpect(status().isBadRequest());

        List<Movie> movies = movieRepository.findAll();
        assertThat(movies).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = movieRepository.findAll().size();
        // set the field null
        movie.setYear(null);

        // Create the Movie, which fails.

        restMovieMockMvc.perform(post("/api/movies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(movie)))
                .andExpect(status().isBadRequest());

        List<Movie> movies = movieRepository.findAll();
        assertThat(movies).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = movieRepository.findAll().size();
        // set the field null
        movie.setDuration(null);

        // Create the Movie, which fails.

        restMovieMockMvc.perform(post("/api/movies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(movie)))
                .andExpect(status().isBadRequest());

        List<Movie> movies = movieRepository.findAll();
        assertThat(movies).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = movieRepository.findAll().size();
        // set the field null
        movie.setDescription(null);

        // Create the Movie, which fails.

        restMovieMockMvc.perform(post("/api/movies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(movie)))
                .andExpect(status().isBadRequest());

        List<Movie> movies = movieRepository.findAll();
        assertThat(movies).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAgeRatingIsRequired() throws Exception {
        int databaseSizeBeforeTest = movieRepository.findAll().size();
        // set the field null
        movie.setAgeRating(null);

        // Create the Movie, which fails.

        restMovieMockMvc.perform(post("/api/movies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(movie)))
                .andExpect(status().isBadRequest());

        List<Movie> movies = movieRepository.findAll();
        assertThat(movies).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMovies() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movies
        restMovieMockMvc.perform(get("/api/movies?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(movie.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
                .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.doubleValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].ageRating").value(hasItem(DEFAULT_AGE_RATING)));
    }

    @Test
    @Transactional
    public void getMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get the movie
        restMovieMockMvc.perform(get("/api/movies/{id}", movie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(movie.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.ageRating").value(DEFAULT_AGE_RATING));
    }

    @Test
    @Transactional
    public void getNonExistingMovie() throws Exception {
        // Get the movie
        restMovieMockMvc.perform(get("/api/movies/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMovie() throws Exception {
        // Initialize the database
        movieService.save(movie);

        int databaseSizeBeforeUpdate = movieRepository.findAll().size();

        // Update the movie
        Movie updatedMovie = movieRepository.findOne(movie.getId());
        updatedMovie
                .title(UPDATED_TITLE)
                .year(UPDATED_YEAR)
                .duration(UPDATED_DURATION)
                .description(UPDATED_DESCRIPTION)
                .ageRating(UPDATED_AGE_RATING);

        restMovieMockMvc.perform(put("/api/movies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMovie)))
                .andExpect(status().isOk());

        // Validate the Movie in the database
        List<Movie> movies = movieRepository.findAll();
        assertThat(movies).hasSize(databaseSizeBeforeUpdate);
        Movie testMovie = movies.get(movies.size() - 1);
        assertThat(testMovie.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMovie.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testMovie.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testMovie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMovie.getAgeRating()).isEqualTo(UPDATED_AGE_RATING);
    }

    @Test
    @Transactional
    public void deleteMovie() throws Exception {
        // Initialize the database
        movieService.save(movie);

        int databaseSizeBeforeDelete = movieRepository.findAll().size();

        // Get the movie
        restMovieMockMvc.perform(delete("/api/movies/{id}", movie.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Movie> movies = movieRepository.findAll();
        assertThat(movies).hasSize(databaseSizeBeforeDelete - 1);
    }
}
