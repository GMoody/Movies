package org.movies.gm.repository;

import org.movies.gm.domain.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the Actor entity.
 */
@SuppressWarnings("unused")
public interface ActorRepository extends JpaRepository<Actor,Long> {

    @Query("select actor from Actor actor left join fetch actor.movies where actor.id =:id")
    Actor findOneWithEagerRelationships(@Param("id") Long id);
}
