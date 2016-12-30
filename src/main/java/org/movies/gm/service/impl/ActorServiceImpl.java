package org.movies.gm.service.impl;

import org.movies.gm.domain.Movie;
import org.movies.gm.service.ActorService;
import org.movies.gm.domain.Actor;
import org.movies.gm.repository.ActorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Actor.
 */
@Service
@Transactional
public class ActorServiceImpl implements ActorService{

    private final Logger log = LoggerFactory.getLogger(ActorServiceImpl.class);

    @Inject
    private ActorRepository actorRepository;

    /**
     * Save a actor.
     *
     * @param actor the entity to save
     * @return the persisted entity
     */
    public Actor save(Actor actor) {
        log.debug("Request to save Actor : {}", actor);
        Optional<Actor> requested = actorRepository.
            findByFirstNameAndLastNameIgnoreCase(actor.getFirstName(), actor.getLastName());
        if(!requested.isPresent()) return actorRepository.save(actor);
        else return requested.get();
    }

    /**
     *  Get all the actors.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Actor> findAll(Pageable pageable) {
        log.debug("Request to get all Actors by page");
        Page<Actor> result = actorRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one actor by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Actor findOne(Long id) {
        log.debug("Request to get Actor : {}", id);
        Actor actor = actorRepository.findOne(id);
        return actor;
    }

    /**
     *  Delete the  actor by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Actor : {}", id);
        Actor actor = actorRepository.findOneWithEagerRelationships(id);
        actor.getMovies().forEach(m -> m.getActors().remove(actor));
        actorRepository.delete(id);
    }

    @Override
    public List<Actor> findAll() {
        log.debug("Request to get all Actors");
        List<Actor> result = actorRepository.findAll();
        return result;
    }

    @Override
    public List<Movie> findActorMovies(Long id) {
        log.debug("Request to get Actor movies");
        Actor actor = actorRepository.findOneWithEagerRelationships(id);
        List<Movie> movies = new ArrayList<>();
        movies.addAll(actor.getMovies());
        return movies;
    }
}
