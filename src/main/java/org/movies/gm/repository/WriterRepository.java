package org.movies.gm.repository;

import org.movies.gm.domain.Writer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Writer entity.
 */
@SuppressWarnings("unused")
public interface WriterRepository extends JpaRepository<Writer,Long> {

    @Query("select writer from Writer writer left join fetch writer.movies where writer.id =:id")
    Writer findOneWithEagerRelationships(@Param("id") Long id);

    Optional<Writer> findByFirstNameAndLastNameIgnoreCase(String firstName, String lastName);
}
