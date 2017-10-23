package org.movies.gm.service;

import org.movies.gm.domain.Movie;
import org.movies.gm.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * Service Interface for managing Movie.
 */
public interface MovieService {

    /**
     * Save a movie.
     *
     * @param movie the entity to save
     * @return the persisted entity
     */
    Movie save(Movie movie);

    /**
     *  Get all the movies.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Movie> findAll(Pageable pageable);

    /**
     *  Get the "id" movie.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Movie findOne(Long id);

    /**
     *  Delete the "id" movie.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /*
     * TODO: Comment methods
     */
    Movie addCurrentFollower(Long movieID);
    Movie removeCurrentFollower(Long movieID);
    Set<User> getMovieFollowers(Long id);
    Page<Movie> getMoviesByGenreId(Long id, Pageable pageable);
    Page<Movie> getMoviesByCurrentUser(Pageable pageable);
    Page<Movie> getMoviesByCurrentUserAndGenre(Long id, Pageable pageable);
}
