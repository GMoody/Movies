package org.movies.gm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.movies.gm.domain.Director;
import org.movies.gm.domain.Movie;
import org.movies.gm.service.DirectorService;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Director.
 */
@RestController
@RequestMapping("/api")
public class DirectorResource {

    private final Logger log = LoggerFactory.getLogger(DirectorResource.class);

    @Inject
    private DirectorService directorService;

    /**
     * POST  /directors : Create a new director.
     *
     * @param director the director to create
     * @return the ResponseEntity with status 201 (Created) and with body the new director, or with status 400 (Bad Request) if the director has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/directors")
    @Timed
    public ResponseEntity<Director> createDirector(@Valid @RequestBody Director director) throws URISyntaxException {
        log.debug("REST request to save Director : {}", director);
        if (director.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("director", "idexists", "A new director cannot already have an ID")).body(null);
        }
        Director result = directorService.save(director);
        return ResponseEntity.created(new URI("/api/directors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("director", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /directors : Updates an existing director.
     *
     * @param director the director to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated director,
     * or with status 400 (Bad Request) if the director is not valid,
     * or with status 500 (Internal Server Error) if the director couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/directors")
    @Timed
    public ResponseEntity<Director> updateDirector(@Valid @RequestBody Director director) throws URISyntaxException {
        log.debug("REST request to update Director : {}", director);
        if (director.getId() == null) {
            return createDirector(director);
        }
        Director result = directorService.save(director);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("director", director.getId().toString()))
            .body(result);
    }

    /**
     * GET  /directors : get all the directors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of directors in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/directors")
    @Timed
    public ResponseEntity<List<Director>> getAllDirectors(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Directors");
        Page<Director> page = directorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/directors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/directors/all")
    @Timed
    public ResponseEntity<List<Director>> getAllDirectors(){
        log.debug("REST request to get all Directors");
        List<Director> directors = directorService.findAll();
        directors.sort(Comparator.comparing(Director::getFirstName));
        return new ResponseEntity<>(directors, HttpStatus.OK);
    }

    /**
     * GET  /directors/:id : get the "id" director.
     *
     * @param id the id of the director to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the director, or with status 404 (Not Found)
     */
    @GetMapping("/directors/{id}")
    @Timed
    public ResponseEntity<Director> getDirector(@PathVariable Long id) {
        log.debug("REST request to get Director : {}", id);
        Director director = directorService.findOne(id);
        return Optional.ofNullable(director)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/directors/{id}/movies")
    @Timed
    public ResponseEntity<List<Movie>> getDirectorMovies(@PathVariable Long id) {
        log.debug("REST request to get director movies");
        return new ResponseEntity<>(directorService.findDirectorMovies(id), HttpStatus.OK);
    }

    /**
     * DELETE  /directors/:id : delete the "id" director.
     *
     * @param id the id of the director to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/directors/{id}")
    @Timed
    public ResponseEntity<Void> deleteDirector(@PathVariable Long id) {
        log.debug("REST request to delete Director : {}", id);
        directorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("director", id.toString())).build();
    }

}
