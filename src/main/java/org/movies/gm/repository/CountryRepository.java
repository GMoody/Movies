package org.movies.gm.repository;

import org.movies.gm.domain.Country;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Country entity.
 */
@SuppressWarnings("unused")
public interface CountryRepository extends JpaRepository<Country,Long> {

    @Query("select country from Country country left join fetch country.movies where country.id =:id")
    Country findOneWithEagerRelationships(@Param("id") Long id);

    Optional<Country> findByTitleIgnoreCase(String title);
}
