package org.movies.gm.service.impl;

import org.movies.gm.domain.Genre;
import org.movies.gm.repository.GenreRepository;
import org.movies.gm.repository.MovieRepository;
import org.movies.gm.service.GenreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.*;

/**
 * Service Implementation for managing Genre.
 */
@Service
@Transactional
public class GenreServiceImpl implements GenreService{

    private final Logger log = LoggerFactory.getLogger(GenreServiceImpl.class);

    @Inject
    private GenreRepository genreRepository;

    @Inject
    private MovieRepository movieRepository;

    /**
     * Save a genre.
     *
     * @param genre the entity to save
     * @return the persisted entity
     */
    public Genre save(Genre genre) {
        log.debug("Request to save Genre : {}", genre);
        Optional<Genre> requested = genreRepository.findByTitleIgnoreCase(genre.getTitle());
        if(!requested.isPresent()) return genreRepository.save(genre);
        else return requested.get();
    }

    /**
     *  Get all the genres.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Genre> findAll(Pageable pageable) {
        log.debug("Request to get all Genres by page");
        Page<Genre> result = genreRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one genre by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Genre findOne(Long id) {
        log.debug("Request to get Genre : {}", id);
        Genre genre = genreRepository.findOneWithEagerRelationships(id);
        return genre;
    }

    /**
     *  Delete the  genre by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Genre : {}", id);
        Genre genre = genreRepository.findOneWithEagerRelationships(id);
        genre.getMovies().forEach(m -> m.getGenres().remove(genre));
        genreRepository.delete(id);
    }

    @Override
    public List<Genre> findAll() {
        log.debug("Request to get all Genres");
        List<Genre> result = genreRepository.findAll();
        return result;
    }

    @Override
    public List<Genre> findUsedGenres() {
        log.debug("Request to get used Genres");
        Set<Genre> genres = new HashSet<>();
        movieRepository.findAllWithEagerRelationships().forEach(m -> genres.addAll(m.getGenres()));
        List<Genre> returned = new ArrayList<>();
        returned.addAll(genres);
        return returned;
    }
}
