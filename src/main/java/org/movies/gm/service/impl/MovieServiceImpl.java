package org.movies.gm.service.impl;

import org.movies.gm.domain.Movie;
import org.movies.gm.domain.User;
import org.movies.gm.repository.MovieRepository;
import org.movies.gm.repository.UserRepository;
import org.movies.gm.security.SecurityUtils;
import org.movies.gm.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing Movie.
 */
@Service
@Transactional
public class MovieServiceImpl implements MovieService{

    private final Logger log = LoggerFactory.getLogger(MovieServiceImpl.class);

    @Inject
    private MovieRepository movieRepository;

    @Inject
    UserRepository userRepository;

    /**
     * Save a movie.
     *
     * @param movie the entity to save
     * @return the persisted entity
     */
    public Movie save(Movie movie) {
        log.debug("Request to save Movie : {}", movie);
        Movie result = movieRepository.save(movie);
        return result;
    }

    /**
     *  Get all the movies.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Movie> findAll(Pageable pageable) {
        log.debug("Request to get all Movies by page");
        Page<Movie> result = movieRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one movie by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Movie findOne(Long id) {
        log.debug("Request to get Movie : {}", id);
        Movie movie = movieRepository.findOneWithEagerRelationships(id);
        return movie;
    }

    /**
     *  Delete the  movie by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Movie : {}", id);
        movieRepository.delete(id);
    }

    @Override
    public Movie addCurrentFollower(Long movieID) {
        log.debug("Request to add current user to movie {" + movieID + "} as follower");
        Movie movie = movieRepository.findOneWithEagerRelationships(movieID);
        Optional<User> user = userRepository.findOneWithEagerRelationshipsByLogin(SecurityUtils.getCurrentUserLogin());
        if(user.isPresent() && !movie.getFollowers().contains(user.get())){
            movie.addFollower(user.get());
            user.get().addFavouriteMovie(movie);
        }
        return movie;
    }

    @Override
    public Movie removeCurrentFollower(Long movieID) {
        log.debug("Request to remove current user from movie {" + movieID + "}");
        Movie movie = movieRepository.findOneWithEagerRelationships(movieID);
        Optional<User> user = userRepository.findOneWithEagerRelationshipsByLogin(SecurityUtils.getCurrentUserLogin());
        if(user.isPresent() && movie.getFollowers().contains(user.get())){
            movie.removeFollower(user.get());
            user.get().removeFavouriteMovie(movie);
        }
        return movie;
    }

    @Override
    public Set<User> getMovieFollowers(Long id) {
        log.debug("Request to get movie {" + id + "} followers");
        return movieRepository.findOneWithEagerRelationships(id).getFollowers();
    }
}
