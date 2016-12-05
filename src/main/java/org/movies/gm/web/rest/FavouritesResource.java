package org.movies.gm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.movies.gm.domain.Movie;
import org.movies.gm.domain.User;
import org.movies.gm.service.MovieService;
import org.movies.gm.service.UserService;
import org.movies.gm.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class FavouritesResource {

    private final Logger log = LoggerFactory.getLogger(FavouritesResource.class);

    @Inject
    private UserService userService;

    @Inject
    private MovieService movieService;

    @GetMapping("/users/current/favourites")
    @Timed
    public ResponseEntity<Set<Movie>> getCurrentUserFavourites(){
        log.debug("REST request to get current user favourites");
        Optional<User> user = userService.getCurrentEagerUser();
        if(user.isPresent()) return new ResponseEntity<>(user.get().getFavouriteMovies(), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/movies/{movieID}/followers/add")
    @Timed
    public ResponseEntity<Movie> addCurrentFollower(@PathVariable Long movieID){
        log.debug("REST request to add current user to movie {" + movieID + "} as follower");
        if (movieID != null) {
            Movie result = movieService.addCurrentFollower(movieID);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("movie", result.getId().toString()))
                .body(result);
        }
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("movie", "movie not exist", "Movie ID should be valid!")).body(null);
    }

    @DeleteMapping("/movies/{movieID}/followers/remove")
    @Timed
    public ResponseEntity<Movie> removeFollower(@PathVariable Long movieID){
        log.debug("REST request to remove current user from movie {" + movieID + "}");
        if (movieID != null) {
            Movie result = movieService.removeCurrentFollower(movieID);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityDeletionAlert("movie", result.getId().toString()))
                .body(result);
        }
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("movie", "movie not exist", "Movie ID should be valid!")).body(null);
    }

    @GetMapping("/movies/{id}/followers")
    @Timed
    public ResponseEntity<Set<User>> getMovieFollowers(@PathVariable Long id){
        log.debug("REST request to get movie {" + id + "} followers");
        Set<User> followers = movieService.getMovieFollowers(id);
        return new ResponseEntity<>(followers, HttpStatus.OK);
    }
}
