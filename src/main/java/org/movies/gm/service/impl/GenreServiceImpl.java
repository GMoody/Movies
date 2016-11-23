package org.movies.gm.service.impl;

import org.movies.gm.service.GenreService;
import org.movies.gm.domain.Genre;
import org.movies.gm.repository.GenreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Genre.
 */
@Service
@Transactional
public class GenreServiceImpl implements GenreService{

    private final Logger log = LoggerFactory.getLogger(GenreServiceImpl.class);
    
    @Inject
    private GenreRepository genreRepository;

    /**
     * Save a genre.
     *
     * @param genre the entity to save
     * @return the persisted entity
     */
    public Genre save(Genre genre) {
        log.debug("Request to save Genre : {}", genre);
        Genre result = genreRepository.save(genre);
        return result;
    }

    /**
     *  Get all the genres.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Genre> findAll(Pageable pageable) {
        log.debug("Request to get all Genres");
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
        Genre genre = genreRepository.findOne(id);
        return genre;
    }

    /**
     *  Delete the  genre by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Genre : {}", id);
        genreRepository.delete(id);
    }
}
