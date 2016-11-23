package org.movies.gm.repository;

import org.movies.gm.domain.Writer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Writer entity.
 */
@SuppressWarnings("unused")
public interface WriterRepository extends JpaRepository<Writer,Long> {

}
