package validators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import databases.Application;
import databases.Database;
import entities.Movie;
import entities.Notification;
import entities.User;
import comparators.DurationComparator;
import comparators.FiltersComparator;
import comparators.RatingComparator;
import input.ActionInput;
import input.ContainsInput;
import input.SortInput;
import java.util.Comparator;

public final class VerifyMovies {

    private VerifyMovies() {
    }

    private static boolean canView(final Movie movie, final String country) {
        if (movie.getBannedCountries().contains(country)) {
            return false;
        }
        return true;
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
    public static ObjectNode showOutput(final Application application, final Database database) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();
        ArrayNode movieListOutput = objectMapper.createArrayNode();
        ObjectNode error = null;

        result.set("error", error);
        application.getCurrentUser().removeCurrentMovies();
        for (Movie movie : database.getMovies()) {
            if (canView(movie, application.getCurrentUser().getCountry())) {
                application.getCurrentUser().addCurrentMovie(movie);
            }
        }

        for (Movie movie : database.getMovies()) {
            if (canView(movie, application.getCurrentUser().getCountry())) {
                movieListOutput.add(showMovie(movie));
            }
        }

        result.set("currentMoviesList", movieListOutput);

        result.set("currentUser", showUser(application.getCurrentUser()));

        return result;
    }

    /**
     * The method checks if the title of the given movie starts with a certain
     * prefix
     * @param movie: the movie that is being checked
     * @param title: the prefix that is supposed to be found at the beginning
     * of the movie's name
     * @return boolean: "true" if the condition is met, "false" otherwise
     */
    private static boolean search(final Movie movie, final String title) {
        if (movie.getName().startsWith(title)) {
            return true;
        }
        return false;
    }

    /**
     * The method the output that is shown when the "search" action takes place
     * @param application: the platform's hierarchy of pages that holds
     * the required information
     * @param database: the platform's database the page makes use of
     * @param action: the details of the action that is supposed to happen
     * @return result: the ObjectNode that will further be added to the
     * JSON ArrayNode that holds the output for the whole application
     */
    private static ObjectNode showSearch(final Application application, final Database database,
                                                                        final ActionInput action) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();
        ArrayNode movieListOutput = objectMapper.createArrayNode();
        ObjectNode error = null;

        result.set("error", error);
        application.getCurrentUser().removeCurrentMovies();
        for (Movie movie : database.getMovies()) {
            if (canView(movie, application.getCurrentUser().getCountry())
                                    && search(movie, action.getStartsWith())) {
                movieListOutput.add(showMovie(movie));
                application.getCurrentUser().addCurrentMovie(movie);
            }
        }
        result.set("currentMoviesList", movieListOutput);

        result.set("currentUser", showUser(application.getCurrentUser()));

        return result;
    }

    /**
     * The method sorts the list of movies the user sees on the screen
     * @param application: the platform's hierarchy of pages that holds
     * the required information
     * @param sortInput: the sorting criteria
     */
    private static void filterSort(final Application application, final SortInput sortInput) {

        Comparator<Movie> comparator = null;

        if (sortInput.getRating() == null && sortInput.getDuration() != null) {
            comparator = new DurationComparator(sortInput.getDuration());
        }

        if (sortInput.getRating() != null && sortInput.getDuration() == null) {
            comparator = new RatingComparator(sortInput.getRating());
        }

        if (sortInput.getRating() != null && sortInput.getDuration() != null) {
            comparator = new FiltersComparator(sortInput.getRating(), sortInput.getDuration());
        }

        application.getCurrentUser().getCurrentMovieList().sort(comparator);
    }

    /**
     * The method checks if the chosen movie has all the mentioned genres
     * @param movie: the movie that is being checked
     * @param containsInput: the criteria that is applied
     * @return boolean: "true" if the movie meets the condition
     * and false otherwise
     */
    private static boolean applyGenre(final Movie movie, final ContainsInput containsInput) {
        for (String genre : containsInput.getGenre()) {
            if (!movie.getGenres().contains(genre)) {
                return false;
            }
        }
        return true;
    }

    /**
     * The method checks if the chosen movie has all the mentioned actors
     * @param movie: the movie that is being checked
     * @param containsInput: the criteria that is applied
     * @return boolean: "true" if the movie meets the condition
     * and false otherwise
     */
    private static boolean applyActors(final Movie movie, final ContainsInput containsInput) {
        for (String actor : containsInput.getActors()) {
            if (!movie.getActors().contains(actor)) {
                return false;
            }
        }
        return true;
    }

    /**
     * The method applies the filters mentioned above over a movie
     * @param movie: the movie that is analysed
     * @param containsInput: the criteria that is applied
     * @return boolean: "true" if the movie meets the conditions
     * and false otherwise
     */
    private static boolean applyContain(final Movie movie, final ContainsInput containsInput) {
        if (containsInput == null) {
            return true;
        }

        if (containsInput.getGenre() != null && containsInput.getActors() == null) {
            if (applyGenre(movie, containsInput)) {
                return true;
            }
            return false;
        }

        if (containsInput.getGenre() == null && containsInput.getActors() != null) {
            if (applyActors(movie, containsInput)) {
                return true;
            }
            return false;
        }

        if (containsInput.getGenre() != null && containsInput.getActors() != null) {
            if (applyGenre(movie, containsInput) && applyActors(movie, containsInput)) {
                return true;
            }
            return false;
        }

        return false;
    }

    /**
     * The method shows the output that results from applying
     * the desired filters and sort types
     * @param application: the platform's hierarchy of pages that holds
     * the required information
     * @param database:  the platform's database
     * @param action: the action that is supposed to take place
     * @return result: the ObjectNode that holds the requested output
     */
    private static ObjectNode showSort(final Application application, final Database database,
                                                                        final ActionInput action) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();
        ArrayNode movieListOutput = objectMapper.createArrayNode();
        ObjectNode error = null;

        result.set("error", error);
        application.getCurrentUser().removeCurrentMovies();
        for (Movie movie : database.getMovies()) {
            if (canView(movie, application.getCurrentUser().getCountry())
                    && applyContain(movie, action.getFilters().getContains())) {
                application.getCurrentUser().addCurrentMovie(movie);
            }
        }

        if (!application.getCurrentUser().getCurrentMovieList().isEmpty()) {
            if (action.getFilters().getSort() != null) {
                filterSort(application, action.getFilters().getSort());
            }
            for (Movie movie : application.getCurrentUser().getCurrentMovieList()) {
                movieListOutput.add(showMovie(movie));
            }
        }

        result.set("currentMoviesList", movieListOutput);

        result.set("currentUser", showUser(application.getCurrentUser()));

        return result;
    }

    /**
     * The method checks if the given action can take place on
     * the "Movies Page"
     * @param action: the action that is supposed to take place
     * @return boolean: "true" if the conditions are met, "false" otherwise
     */
    private static boolean canAction(final ActionInput action) {
        if (action.getType().compareTo("on page") != 0) {
            return false;
        }

        if (action.getFeature().compareTo("search") != 0
                && action.getFeature().compareTo("filter") != 0) {
            return false;
        }

        return true;
    }

    /**
     * The method executes the specific actions of the "Movies Page"
     * @param database: the platform's database
     * @param application: the platform's hierarchy of pages that holds
     * the required information
     * @param action: the action that should take place
     * @param output: the JSON ArrayNode that will contain the information
     * that is requested when fulfilling the action
     * @return boolean: "true" if the action fif take place, "false" otherwise
     */
    public static boolean moviesAction(final Database database, final Application application,
                                                final ActionInput action, final ArrayNode output) {

        if (!canAction(action)) {
            return false;
        }

        if (action.getFeature().compareTo("search") == 0) {
            output.add(showSearch(application, database, action));
        }

        if (action.getFeature().compareTo("filter") == 0) {
            output.add(showSort(application, database, action));
        }

        return true;
    }

    /**
     * The method checks if the navigation to a chosen page is possible,
     * according to the platform's rules (from the "Movies Page")
     * @param action: the action that is supposed to take place
     * @return boolean: "true" if the page switch is possible,
     * "false" otherwise
     */
    public static boolean canNavigate(final Application application, final ActionInput action) {
        if (action.getType().compareTo("change page") != 0) {
            return false;
        }

        if (action.getPage().compareTo("see details") != 0
                && action.getPage().compareTo("logout") != 0
                    && action.getPage().compareTo("homepage") != 0
                        && action.getPage().compareTo("upgrades") != 0) {
            return false;
        }

        if (action.getPage().compareTo("see details") == 0) {
            for (Movie movie : application.getCurrentUser().getCurrentMovieList()) {
                if (movie.getName().compareTo(action.getMovie()) == 0) {
                    return true;
                }
            }
        }

        if (action.getPage().compareTo("logout") == 0) {
            return true;
        }

        return false;
    }

    /**
     * The method searches for a certain movie from the ones that the user
     * can see on the screen
     * @param application: the platform's hierarchy of pages that holds
     * the required information
     * @param action: the details of the movie that is being searched
     * @return user: the found movie or null, if it had not been appeared
     * on the screen
     */
    public static Movie retrieveMovie(final Application application, final ActionInput action) {
        for (Movie movie : application.getCurrentUser().getCurrentMovieList()) {
            if (movie.getName().compareTo(action.getMovie()) == 0) {
                return movie;
            }
        }

        return null;
    }

    /**
     * The method creates the output that is shown if the page action
     * cannot take place
     * @return result: the ObjectNode that will further be added to the
     * JSON ArrayNode that holds the output for the whole application
     */
    public static ObjectNode showActionError() {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();
        ArrayNode movieListOutput = objectMapper.createArrayNode();
        ObjectNode userOutput = null;

        result.put("error", "Error");
        result.set("currentMoviesList", movieListOutput);
        result.set("currentUser", userOutput);

        return result;
    }
}
