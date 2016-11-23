package org.movies.gm.service;

import org.movies.gm.domain.Writer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Writer.
 */
public interface WriterService {

    /**
     * Save a writer.
     *
     * @param writer the entity to save
     * @return the persisted entity
     */
    Writer save(Writer writer);

    /**
     *  Get all the writers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Writer> findAll(Pageable pageable);

    /**
     *  Get the "id" writer.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Writer findOne(Long id);

    /**
     *  Delete the "id" writer.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
