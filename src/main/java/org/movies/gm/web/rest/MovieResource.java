package org.movies.gm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.movies.gm.domain.Movie;
import org.movies.gm.domain.User;
import org.movies.gm.service.MovieService;
import org.movies.gm.service.UserService;
import org.movies.gm.web.rest.util.HeaderUtil;
import org.movies.gm.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing Movie.
 */
@RestController
@RequestMapping("/api")
public class MovieResource {

    private final Logger log = LoggerFactory.getLogger(MovieResource.class);

    @Inject
    private MovieService movieService;

    @Inject
    private UserService userService;

    /**
     * POST  /movies : Create a new movie.
     *
     * @param movie the movie to create
     * @return the ResponseEntity with status 201 (Created) and with body the new movie, or with status 400 (Bad Request) if the movie has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/movies")
    @Timed
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody Movie movie) throws URISyntaxException {
        log.debug("REST request to save Movie : {}", movie);
        if (movie.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("movie", "idexists", "A new movie cannot already have an ID")).body(null);
        }
        Movie result = movieService.save(movie);
        return ResponseEntity.created(new URI("/api/movies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("movie", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /movies : Updates an existing movie.
     *
     * @param movie the movie to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated movie,
     * or with status 400 (Bad Request) if the movie is not valid,
     * or with status 500 (Internal Server Error) if the movie couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/movies")
    @Timed
    public ResponseEntity<Movie> updateMovie(@Valid @RequestBody Movie movie) throws URISyntaxException {
        log.debug("REST request to update Movie : {}", movie);
        if (movie.getId() == null) {
            return createMovie(movie);
        }
        Movie result = movieService.save(movie);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("movie", movie.getId().toString()))
            .body(result);
    }

    /**
     * GET  /movies : get all the movies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of movies in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/movies")
    @Timed
    public ResponseEntity<List<Movie>> getAllMovies(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Movies");
        Page<Movie> page = movieService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/movies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /movies/:id : get the "id" movie.
     *
     * @param id the id of the movie to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the movie, or with status 404 (Not Found)
     */
    @GetMapping("/movies/{id}")
    @Timed
    public ResponseEntity<Movie> getMovie(@PathVariable Long id) {
        log.debug("REST request to get Movie : {}", id);
        Movie movie = movieService.findOne(id);
        return Optional.ofNullable(movie)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /movies/:id : delete the "id" movie.
     *
     * @param id the id of the movie to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/movies/{id}")
    @Timed
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        log.debug("REST request to delete Movie : {}", id);
        movieService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("movie", id.toString())).build();
    }

    @PostMapping("/movies/followers/{movieID}/{userLogin}")
    @Timed
    public ResponseEntity<Movie> addFollower(@PathVariable Long movieID, @PathVariable String userLogin){
        log.debug("REST request to add follower {" + userLogin + "} to movie {" + movieID + "}");
        if (movieID != null && !userLogin.isEmpty()) {
            Optional<User> requestedUser = userService.getUserWithAuthoritiesByLogin(userLogin);
            Movie result = movieService.addFollower(movieID, requestedUser.get().getId());
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("movie", result.getId().toString()))
                .body(result);
        }
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("movie", "movie || user not exist", "Movie and User IDs should be valid!")).body(null);
    }

    @DeleteMapping("/movies/followers/{movieID}/{userLogin}")
    @Timed
    public ResponseEntity<Movie> removeFollower(@PathVariable Long movieID, @PathVariable String userLogin){
        log.debug("REST request to remove follower {" + userLogin + "} from movie {" + movieID + "}");
        if (movieID != null && !userLogin.isEmpty()) {
            Optional<User> requestedUser = userService.getUserWithAuthoritiesByLogin(userLogin);
            Movie result = movieService.removeFollower(movieID, requestedUser.get().getId());
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityDeletionAlert("movie", result.getId().toString()))
                .body(result);
        }
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("movie", "movie || user not exist", "Movie and User IDs should be valid!")).body(null);
    }

    @GetMapping("/movies/{id}/followers")
    @Timed
    public ResponseEntity<Set<User>> getMovieFollowers(@PathVariable Long id){
        log.debug("REST request to get movie {" + id + "} followers");
        Set<User> followers = movieService.getMovieFollowers(id);
        return new ResponseEntity<>(followers, HttpStatus.OK);
    }
}
