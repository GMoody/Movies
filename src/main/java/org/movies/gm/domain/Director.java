package org.movies.gm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Director.
 */
@Entity
@Table(name = "director")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Director implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "avatar_url")
    private String avatarURL;

    @ManyToMany(mappedBy = "directors")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Movie> movies = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Director firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Director lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public Director movies(Set<Movie> movies) {
        this.movies = movies;
        return this;
    }

    public Director addMovie(Movie movie) {
        movies.add(movie);
        movie.getDirectors().add(this);
        return this;
    }

    public Director removeMovie(Movie movie) {
        movies.remove(movie);
        movie.getDirectors().remove(this);
        return this;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Director director = (Director) o;
        if(director.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, director.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Director{" +
            "id=" + id +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            '}';
    }
}
