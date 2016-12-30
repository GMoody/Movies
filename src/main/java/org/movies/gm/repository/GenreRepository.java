package org.movies.gm.repository;

import org.movies.gm.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Genre entity.
 */
@SuppressWarnings("unused")
public interface GenreRepository extends JpaRepository<Genre,Long> {

    @Query("select genre from Genre genre left join fetch genre.movies where genre.id =:id")
    Genre findOneWithEagerRelationships(@Param("id") Long id);

    Optional<Genre> findByTitleIgnoreCase(String title);
}
