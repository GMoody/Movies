package org.movies.gm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.movies.gm.domain.Genre;
import org.movies.gm.service.GenreService;
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

/**
 * REST controller for managing Genre.
 */
@RestController
@RequestMapping("/api")
public class GenreResource {

    private final Logger log = LoggerFactory.getLogger(GenreResource.class);
        
    @Inject
    private GenreService genreService;

    /**
     * POST  /genres : Create a new genre.
     *
     * @param genre the genre to create
     * @return the ResponseEntity with status 201 (Created) and with body the new genre, or with status 400 (Bad Request) if the genre has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/genres")
    @Timed
    public ResponseEntity<Genre> createGenre(@Valid @RequestBody Genre genre) throws URISyntaxException {
        log.debug("REST request to save Genre : {}", genre);
        if (genre.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("genre", "idexists", "A new genre cannot already have an ID")).body(null);
        }
        Genre result = genreService.save(genre);
        return ResponseEntity.created(new URI("/api/genres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("genre", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /genres : Updates an existing genre.
     *
     * @param genre the genre to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated genre,
     * or with status 400 (Bad Request) if the genre is not valid,
     * or with status 500 (Internal Server Error) if the genre couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/genres")
    @Timed
    public ResponseEntity<Genre> updateGenre(@Valid @RequestBody Genre genre) throws URISyntaxException {
        log.debug("REST request to update Genre : {}", genre);
        if (genre.getId() == null) {
            return createGenre(genre);
        }
        Genre result = genreService.save(genre);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("genre", genre.getId().toString()))
            .body(result);
    }

    /**
     * GET  /genres : get all the genres.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of genres in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/genres")
    @Timed
    public ResponseEntity<List<Genre>> getAllGenres(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Genres");
        Page<Genre> page = genreService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/genres");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /genres/:id : get the "id" genre.
     *
     * @param id the id of the genre to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the genre, or with status 404 (Not Found)
     */
    @GetMapping("/genres/{id}")
    @Timed
    public ResponseEntity<Genre> getGenre(@PathVariable Long id) {
        log.debug("REST request to get Genre : {}", id);
        Genre genre = genreService.findOne(id);
        return Optional.ofNullable(genre)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /genres/:id : delete the "id" genre.
     *
     * @param id the id of the genre to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/genres/{id}")
    @Timed
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        log.debug("REST request to delete Genre : {}", id);
        genreService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("genre", id.toString())).build();
    }

}
