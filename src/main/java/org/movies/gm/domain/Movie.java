package org.movies.gm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Movie.
 */
@Entity
@Table(name = "movie")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQuery(name = "Movie.findMoviesByGenre",
    query = "select movie from Movie movie where ?1 member of movie.genres")
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 5, max = 100)
    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @NotNull
    @Min(value = 1890)
    @Column(name = "year", nullable = false)
    private Integer year;

    @NotNull
    @DecimalMin(value = "0.1")
    @DecimalMax(value = "10")
    @Column(name = "duration", nullable = false)
    private Double duration;

    @NotNull
    @Size(min = 10, max = 1000)
    @Column(name = "description", length = 1000, nullable = false)
    private String description;

    @NotNull
    @Min(value = 1)
    @Max(value = 18)
    @Column(name = "age_rating", nullable = false)
    private Integer ageRating;

    @Column(name = "avatar_url")
    private String avatarURL;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "movie_director",
        joinColumns = @JoinColumn(name = "movies_id", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "directors_id", referencedColumnName = "ID"))
    private Set<Director> directors = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "movie_writer",
        joinColumns = @JoinColumn(name = "movies_id", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "writers_id", referencedColumnName = "ID"))
    private Set<Writer> writers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "movie_genre",
        joinColumns = @JoinColumn(name = "movies_id", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "genres_id", referencedColumnName = "ID"))
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "movie_actor",
        joinColumns = @JoinColumn(name = "movies_id", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "actors_id", referencedColumnName = "ID"))
    private Set<Actor> actors = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "movie_country",
        joinColumns = @JoinColumn(name = "movies_id", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "countries_id", referencedColumnName = "ID"))
    private Set<Country> countries = new HashSet<>();

    @ManyToMany(mappedBy = "favouriteMovies")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<User> followers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Movie title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public Movie year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getDuration() {
        return duration;
    }

    public Movie duration(Double duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public Movie description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAgeRating() {
        return ageRating;
    }

    public Movie ageRating(Integer ageRating) {
        this.ageRating = ageRating;
        return this;
    }

    public void setAgeRating(Integer ageRating) {
        this.ageRating = ageRating;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public Set<Director> getDirectors() {
        return directors;
    }

    public Movie directors(Set<Director> directors) {
        this.directors = directors;
        return this;
    }

    public Movie addDirector(Director director) {
        directors.add(director);
        director.getMovies().add(this);
        return this;
    }

    public Movie removeDirector(Director director) {
        directors.remove(director);
        director.getMovies().remove(this);
        return this;
    }

    public void setDirectors(Set<Director> directors) {
        this.directors = directors;
    }

    public Set<Writer> getWriters() {
        return writers;
    }

    public Movie writers(Set<Writer> writers) {
        this.writers = writers;
        return this;
    }

    public Movie addWriter(Writer writer) {
        writers.add(writer);
        writer.getMovies().add(this);
        return this;
    }

    public Movie removeWriter(Writer writer) {
        writers.remove(writer);
        writer.getMovies().remove(this);
        return this;
    }

    public void setWriters(Set<Writer> writers) {
        this.writers = writers;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public Movie genres(Set<Genre> genres) {
        this.genres = genres;
        return this;
    }

    public Movie addGenre(Genre genre) {
        genres.add(genre);
        genre.getMovies().add(this);
        return this;
    }

    public Movie removeGenre(Genre genre) {
        genres.remove(genre);
        genre.getMovies().remove(this);
        return this;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public Movie actors(Set<Actor> actors) {
        this.actors = actors;
        return this;
    }

    public Movie addActor(Actor actor) {
        actors.add(actor);
        actor.getMovies().add(this);
        return this;
    }

    public Movie removeActor(Actor actor) {
        actors.remove(actor);
        actor.getMovies().remove(this);
        return this;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }

    public Set<Country> getCountries() {
        return countries;
    }

    public Movie countries(Set<Country> countries) {
        this.countries = countries;
        return this;
    }

    public Movie addCountry(Country country) {
        countries.add(country);
        country.getMovies().add(this);
        return this;
    }

    public Movie removeCountry(Country country) {
        countries.remove(country);
        country.getMovies().remove(this);
        return this;
    }

    public void setCountries(Set<Country> countries) {
        this.countries = countries;
    }

    public Movie followers(Set<User> followers) {
        this.followers = followers;
        return this;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public Movie addFollower(User user) {
        followers.add(user);
        user.getFavouriteMovies().add(this);
        return this;
    }

    public Movie removeFollower(User user) {
        followers.remove(user);
        user.getFavouriteMovies().remove(this);
        return this;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Movie movie = (Movie) o;
        if (movie.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, movie.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Movie{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", year='" + year + "'" +
            ", duration='" + duration + "'" +
            ", description='" + description + "'" +
            ", ageRating='" + ageRating + "'" +
            '}';
    }
}
