package validators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import databases.Application;
import databases.Database;
import entities.Genre;
import entities.Movie;
import entities.Notification;
import entities.User;
import comparators.GenresComparator;
import comparators.LikesComparator;
import java.util.ArrayList;

public final class VerifyRecommendations {

    private VerifyRecommendations() {
    }

    /**
     * The method checks if a movie from the database can be watched by the given user
     * (it is not banned in the user's country, and it has not already been watched by the user)
     * @param movie: the movie that is being checked
     * @param country: the country the user comes from
     * @param watchedMovies: the user's list of watched movies
     * @return boolean: "true" if the movie can be watched, "false" otherwise
     */
    private static boolean canView(final Movie movie, final String country,
                                            final ArrayList<Movie> watchedMovies) {
        if (movie.getBannedCountries().contains(country)) {
            return false;
        }

        if (watchedMovies.contains(movie)) {
            return false;
        }

        return true;
    }

    /**
     * The method checks if a given genre has already been added to
     * a certain user's top of genres
     * @param genres: the list of genres
     * @param checkGenre: the genre that is being searched for
     * @return int: the position at which the genre should be found
     * (0<= position <movies.size(), if the genre is present in the list, -1 otherwise)
     */
    private static int hasGenre(final ArrayList<Genre> genres, final String checkGenre) {

        if (genres.isEmpty()) {
            return -1;
        }

        int i;
        for (i = 0; i < genres.size(); i++) {
            Genre genre = genres.get(i);
            if (genre.getGenre().compareTo(checkGenre) == 0) {
                return i;
            }
        }

        return -1;
    }

    /**
     * The method fulfills outputs the details of a chosen movie
     * @param movie: the movie that is being shown
     * @return result: the JSON node that holds the result
     */
    private static ObjectNode showMovie(final Movie movie) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode movieOutput = objectMapper.createObjectNode();

        movieOutput.put("name", movie.getName());
        movieOutput.put("year", String.valueOf(movie.getYear()));
        movieOutput.put("duration", movie.getDuration());
        ArrayNode genresOutput = objectMapper.createArrayNode();
        for (String genre : movie.getGenres()) {
            genresOutput.add(genre);
        }
        movieOutput.set("genres", genresOutput);
        ArrayNode actorsOutput = objectMapper.createArrayNode();
        for (String actor : movie.getActors()) {
            actorsOutput.add(actor);
        }
        movieOutput.set("actors", actorsOutput);
        ArrayNode countriesOutput = objectMapper.createArrayNode();
        for (String country : movie.getBannedCountries()) {
            countriesOutput.add(country);
        }
        movieOutput.set("countriesBanned", countriesOutput);
        movieOutput.put("numLikes", movie.getTotalLikes());
        movieOutput.put("rating", movie.getRating());
        movieOutput.put("numRatings", movie.getNoRatings());

        return movieOutput;
    }

    /**
     * The method fulfills outputs the details of a given notification
     * @param notification: the notification that is being shown
     * @return result: the JSON node that holds the result
     */
    public static ObjectNode showNotification(final Notification notification) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode notificationOutput = objectMapper.createObjectNode();

        notificationOutput.put("movieName", notification.getMovieName());
        notificationOutput.put("message", notification.getMessage());

        return notificationOutput;
    }

    /**
     * The method fulfills outputs the details of a chosen user
     * @param user: the user that is being shown
     * @return result: the JSON node that holds the result
     */
    private static ObjectNode showUser(final User user) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode userOutput = null;

        if (user.getName() != null) {
            userOutput = objectMapper.createObjectNode();
            ObjectNode credentialsOutput = objectMapper.createObjectNode();
            credentialsOutput.put("name", user.getName());
            credentialsOutput.put("password", user.getPassword());
            credentialsOutput.put("accountType", user.getAccountType());
            credentialsOutput.put("country", user.getCountry());
            credentialsOutput.put("balance", String.valueOf(user.getTotalBalance()));
            userOutput.set("credentials", credentialsOutput);
            userOutput.put("tokensCount", user.getTotalTokens());
            userOutput.put("numFreePremiumMovies", user.getRemainingFreeMovies());

            ArrayNode purchasedOutput = objectMapper.createArrayNode();
            for (Movie movie : user.getPurchasedMovies()) {
                purchasedOutput.add(showMovie(movie));
            }
            userOutput.set("purchasedMovies", purchasedOutput);

            ArrayNode watchedOutput = objectMapper.createArrayNode();
            for (Movie movie : user.getWatchedMovies()) {
                watchedOutput.add(showMovie(movie));
            }
            userOutput.set("watchedMovies", watchedOutput);

            ArrayNode likedOutput = objectMapper.createArrayNode();
            for (Movie movie : user.getLikedMovies()) {
                likedOutput.add(showMovie(movie));
            }
            userOutput.set("likedMovies", likedOutput);

            ArrayNode ratedOutput = objectMapper.createArrayNode();
            for (Movie movie : user.getRatedMovies()) {
                ratedOutput.add(showMovie(movie));
            }
            userOutput.set("ratedMovies", ratedOutput);

            ArrayNode notificationsOutput = objectMapper.createArrayNode();
            for (Notification notification : user.getNotifications()) {
                notificationsOutput.add(showNotification(notification));
            }
            userOutput.set("notifications", notificationsOutput);
        }

        return userOutput;
    }

    /**
     * The method creates the output that is shown if the page action
     * can take place
     * @param application: the platform's hierarchy of pages that holds
     * the required information
     * @return result: the ObjectNode that will further be added to the
     * JSON ArrayNode that holds the output for the whole application
     */
    public static ObjectNode showOutput(final Application application) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();
        ArrayNode movieListOutput = null;
        ObjectNode error = null;

        result.set("error", error);
        result.set("currentMoviesList", movieListOutput);

        result.set("currentUser", showUser(application.getCurrentUser()));

        return result;
    }

    /**
     * The method constructs the top of preferred genres of a user
     * @param application: the platform the user accesses
     * @return genres: the requested list
     */
    private static ArrayList<Genre> getTopOfGenres(final Application application) {

        ArrayList<Genre> genres = new ArrayList<Genre>();

        for (Movie likedMovie : application.getCurrentUser().getLikedMovies()) {
            for (String likedGenre : likedMovie.getGenres()) {

                int position = hasGenre(genres, likedGenre);

                if (position < 0) {
                    Genre newGenre = new Genre(likedGenre);
                    newGenre.addLike();
                    genres.add(newGenre);
                } else {
                    genres.get(position).addLike();
                }
            }
        }

        if (!genres.isEmpty()) {
            genres.sort(new GenresComparator());
        }

        return genres;
    }

    /**
     * The method creates a new notification for the last logged-in "premium" user
     * @param application: the platform that holds the needed information
     * @param database: the database that is accessed
     * @param output: the array of JSON nodes that represents the collection of outputs
     */
    public static void getRecommendation(final Application application, final Database database,
                                                                        final ArrayNode output) {

        if (application.getCurrentUser().getName() == null) {
            return;
        }

        if (application.getCurrentUser().getAccountType().compareTo("standard") == 0) {
            return;
        }

        ArrayList<Genre> genres = getTopOfGenres(application);
        ArrayList<Movie> allMovies = new ArrayList<Movie>();
        String bannedCountry = application.getCurrentUser().getCountry();

        for (Movie movie : database.getMovies()) {
            if (canView(movie, bannedCountry, application.getCurrentUser().getWatchedMovies())) {
                allMovies.add(movie);
            }
        }

        allMovies.sort(new LikesComparator());

        boolean foundMovie = false;
        String searchedMovie = null;
        int i, j;

        for (i = 0; i < genres.size() && !foundMovie; i++) {
            Genre searchedGenre = genres.get(i);

            for (j = 0; j < allMovies.size() && !foundMovie; j++) {

                Movie movie = allMovies.get(j);

                if (movie.getGenres().contains(searchedGenre.getGenre())) {
                    searchedMovie = movie.getName();
                    foundMovie = true;
                }
            }
        }

        Notification newNotification = null;

        if (foundMovie) {
            newNotification = new Notification(searchedMovie, "Recommendation");
        } else {
            newNotification = new Notification("No recommendation", "Recommendation");
        }

        application.getCurrentUser().addNotification(newNotification);

        output.add(showOutput(application));
    }
}
