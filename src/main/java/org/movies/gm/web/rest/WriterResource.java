package org.movies.gm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.movies.gm.domain.Writer;
import org.movies.gm.service.WriterService;
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
 * REST controller for managing Writer.
 */
@RestController
@RequestMapping("/api")
public class WriterResource {

    private final Logger log = LoggerFactory.getLogger(WriterResource.class);

    @Inject
    private WriterService writerService;

    /**
     * POST  /writers : Create a new writer.
     *
     * @param writer the writer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new writer, or with status 400 (Bad Request) if the writer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/writers")
    @Timed
    public ResponseEntity<Writer> createWriter(@Valid @RequestBody Writer writer) throws URISyntaxException {
        log.debug("REST request to save Writer : {}", writer);
        if (writer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("writer", "idexists", "A new writer cannot already have an ID")).body(null);
        }
        Writer result = writerService.save(writer);
        return ResponseEntity.created(new URI("/api/writers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("writer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /writers : Updates an existing writer.
     *
     * @param writer the writer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated writer,
     * or with status 400 (Bad Request) if the writer is not valid,
     * or with status 500 (Internal Server Error) if the writer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/writers")
    @Timed
    public ResponseEntity<Writer> updateWriter(@Valid @RequestBody Writer writer) throws URISyntaxException {
        log.debug("REST request to update Writer : {}", writer);
        if (writer.getId() == null) {
            return createWriter(writer);
        }
        Writer result = writerService.save(writer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("writer", writer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /writers : get all the writers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of writers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/writers")
    @Timed
    public ResponseEntity<List<Writer>> getAllWriters(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Writers");
        Page<Writer> page = writerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/writers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/writers/all")
    @Timed
    public ResponseEntity<List<Writer>> getAllWriters(){
        log.debug("REST request to get all Writers");
        List<Writer> writers = writerService.findAll();
        return new ResponseEntity<>(writers, HttpStatus.OK);
    }

    /**
     * GET  /writers/:id : get the "id" writer.
     *
     * @param id the id of the writer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the writer, or with status 404 (Not Found)
     */
    @GetMapping("/writers/{id}")
    @Timed
    public ResponseEntity<Writer> getWriter(@PathVariable Long id) {
        log.debug("REST request to get Writer : {}", id);
        Writer writer = writerService.findOne(id);
        return Optional.ofNullable(writer)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /writers/:id : delete the "id" writer.
     *
     * @param id the id of the writer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/writers/{id}")
    @Timed
    public ResponseEntity<Void> deleteWriter(@PathVariable Long id) {
        log.debug("REST request to delete Writer : {}", id);
        writerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("writer", id.toString())).build();
    }

}
