package org.movies.gm.repository;

import org.movies.gm.domain.Genre;
import org.movies.gm.domain.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Movie entity.
 */
@SuppressWarnings("unused")
public interface MovieRepository extends JpaRepository<Movie,Long> {

    @Query("select distinct movie from Movie movie left join fetch movie.directors left join fetch movie.writers left join fetch movie.genres left join fetch movie.actors left join fetch movie.countries left join fetch movie.followers")
    List<Movie> findAllWithEagerRelationships();

    @Query("select movie from Movie movie left join fetch movie.directors left join fetch movie.writers left join fetch movie.genres left join fetch movie.actors left join fetch movie.countries left join fetch movie.followers where movie.id =:id")
    Movie findOneWithEagerRelationships(@Param("id") Long id);

    Page<Movie> findMoviesByGenre(@Param("genre")Genre genre, Pageable pageable);
}
