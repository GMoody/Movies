package org.movies.gm.service;

import org.movies.gm.domain.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Genre.
 */
public interface GenreService {

    /**
     * Save a genre.
     *
     * @param genre the entity to save
     * @return the persisted entity
     */
    Genre save(Genre genre);

    /**
     *  Get all the genres.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Genre> findAll(Pageable pageable);

    /**
     *  Get the "id" genre.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Genre findOne(Long id);

    /**
     *  Delete the "id" genre.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<Genre> findAll();
}
