package validators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import databases.Application;
import databases.Database;
import entities.Movie;
import entities.Notification;
import entities.User;
import input.ActionInput;
import input.CredentialsInput;

public final class VerifyRegister {

    private VerifyRegister() {
    }

    /**
     * The method searches for a certain user within the database
     * @param database: the platform's database that is supposed to contain
     * the user's information
     * @param credentials: the details of the user that is being searched
     * @return user: the found user or null, if it had not been registered yet
     */
    private static boolean userNotFound(final Database database,
                                                final CredentialsInput credentials) {
        for (User user : database.getUsers()) {
            if (user.getName().compareTo(credentials.getName()) == 0
                    && user.getPassword().compareTo(credentials.getPassword()) == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * The method implements the action of the "Register Page" if possible
     * @param database: the platform's database the page makes use of
     * @param application: the hierarchy of pages the current one has access to
     * @param action: the details of the action that is supposed to happen
     * @return boolean: "true" if the action can take place, "false" otherwise
     */
    public static boolean registerAction(final Database database, final Application application,
                                                                        final ActionInput action) {

        if (action.getType().compareTo("on page") != 0
                                || action.getFeature().compareTo("register") != 0) {
            return false;
        }

        if (!userNotFound(database, action.getCredentials())) {
            return false;
        }

        database.addUser(action.getCredentials());
        application.setCurrentUser(database.getUsers().get(database.getUsers().size() - 1));
        application.getCurrentUser().auth(application);
        application.userIsLoggedIn();
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
     * cannot take place
     * @param application: the platform's hierarchy of pages that holds
     * the required information
     * @return result: the ObjectNode that will further be added to the
     * JSON ArrayNode that holds the output for the whole application
     */
    public static ObjectNode showError(final Application application) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();
        ArrayNode movieListOutput = objectMapper.createArrayNode();

        result.put("error", "Error");
        if (application.getCurrentUser().getCurrentMovieList().size() != 0) {
            for (Movie movie : application.getCurrentUser().getCurrentMovieList()) {
                movieListOutput.add(showMovie(movie));
            }
        }
        result.set("currentMoviesList", movieListOutput);

        result.set("currentUser", showUser(application.getCurrentUser()));

        return result;
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
        ArrayNode movieListOutput = objectMapper.createArrayNode();
        ObjectNode error = null;

        result.set("error", error);
        if (application.getCurrentUser().getCurrentMovieList().size() != 0) {
            for (Movie movie : application.getCurrentUser().getCurrentMovieList()) {
                movieListOutput.add(showMovie(movie));
            }
        }
        result.set("currentMoviesList", movieListOutput);

        result.set("currentUser", showUser(application.getCurrentUser()));

        return result;
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
