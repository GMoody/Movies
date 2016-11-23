package org.movies.gm.repository;

import org.movies.gm.domain.Actor;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Actor entity.
 */
@SuppressWarnings("unused")
public interface ActorRepository extends JpaRepository<Actor,Long> {

}
