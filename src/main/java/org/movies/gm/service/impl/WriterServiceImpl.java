package org.movies.gm.service.impl;

import org.movies.gm.domain.Movie;
import org.movies.gm.domain.Writer;
import org.movies.gm.repository.WriterRepository;
import org.movies.gm.service.WriterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Writer.
 */
@Service
@Transactional
public class WriterServiceImpl implements WriterService{

    private final Logger log = LoggerFactory.getLogger(WriterServiceImpl.class);

    @Inject
    private WriterRepository writerRepository;

    /**
     * Save a writer.
     *
     * @param writer the entity to save
     * @return the persisted entity
     */
    public Writer save(Writer writer) {
        log.debug("Request to save Writer : {}", writer);
        Optional<Writer> requested = writerRepository.
            findByFirstNameAndLastNameIgnoreCase(writer.getFirstName(), writer.getLastName());
        if(!requested.isPresent()) return writerRepository.save(writer);
        else return requested.get();
    }

    /**
     *  Get all the writers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Writer> findAll(Pageable pageable) {
        log.debug("Request to get all Writers by page");
        Page<Writer> result = writerRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one writer by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Writer findOne(Long id) {
        log.debug("Request to get Writer : {}", id);
        Writer writer = writerRepository.findOne(id);
        return writer;
    }

    /**
     *  Delete the  writer by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Writer : {}", id);
        Writer writer = writerRepository.findOneWithEagerRelationships(id);
        writer.getMovies().forEach(m -> m.getWriters().remove(writer));
        writerRepository.delete(id);
    }

    @Override
    public List<Writer> findAll() {
        log.debug("Request to get all Writers");
        List<Writer> result = writerRepository.findAll();
        return result;
    }

    @Override
    public List<Movie> findWriterMovies(Long id) {
        log.debug("Request to get writer movies");
        Writer writer = writerRepository.findOneWithEagerRelationships(id);
        List<Movie> movies = new ArrayList<>();
        movies.addAll(writer.getMovies());
        return movies;
    }
}
