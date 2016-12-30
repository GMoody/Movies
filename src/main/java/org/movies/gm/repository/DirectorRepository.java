package org.movies.gm.repository;

import org.movies.gm.domain.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Director entity.
 */
@SuppressWarnings("unused")
public interface DirectorRepository extends JpaRepository<Director,Long> {

    @Query("select director from Director director left join fetch director.movies where director.id =:id")
    Director findOneWithEagerRelationships(@Param("id") Long id);

    Optional<Director> findByFirstNameAndLastNameIgnoreCase(String firstName, String lastName);
}
