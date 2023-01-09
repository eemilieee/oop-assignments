package validators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import databases.Application;
import entities.Movie;
import entities.Notification;
import entities.User;
import input.ActionInput;

public final class VerifySeeDetails {

    private static final int MAXIMUM_RATE = 5;

    private VerifySeeDetails() {
    }

    /**
     * The method checks if the navigation to a chosen page is possible,
     * according to the platform's rules (from the "See Details Page")
     * @param action: the action that is supposed to take place
     * @return boolean: "true" if the page switch is possible,
     * "false" otherwise
     */
    public static boolean canNavigate(final ActionInput action) {
        if (action.getType().compareTo("change page") != 0) {
            return false;
        }

        if (action.getPage().compareTo("movies") != 0
                && action.getPage().compareTo("upgrades") != 0
                    && action.getPage().compareTo("logout") != 0) {
            return false;
        }

        return true;
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
    public static ObjectNode showOutput(final Application application, final ActionInput action) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();
        ArrayNode movieListOutput = objectMapper.createArrayNode();
        ObjectNode error = null;

        result.set("error", error);
        Movie retrievedMovie = null;

        if (!application.getCurrentUser().getCurrentMovieList().isEmpty()
                && action.getMovie() != null) {
            for (Movie movie : application.getCurrentUser().getCurrentMovieList()) {
                if (movie.getName().compareTo(action.getMovie()) == 0) {
                    retrievedMovie = movie;
                }
            }
        }

        if (retrievedMovie != null) {
            application.getCurrentUser().removeCurrentMovies();
            application.getCurrentUser().addCurrentMovie(retrievedMovie);
            movieListOutput.add(showMovie(retrievedMovie));
        }

        result.set("currentMoviesList", movieListOutput);

        if (action.getMovie() == null
                && application.getCurrentUser().getCurrentMovieList().size() == 1) {
            movieListOutput.add(showMovie(application.getCurrentUser()
                    .getCurrentMovieList().get(0)));
        }

        result.set("currentUser", showUser(application.getCurrentUser()));

        return result;
    }

    /**
     * The method checks if the "on page" command for the "See Details Page" is valid
     * @param action: the details of the action that is supposed to happen
     * @return boolean: "true" if the action can take place, "false" otherwise
     */
    public static boolean canAction(final ActionInput action) {

        if (action.getType().compareTo("on page") != 0) {
            return false;
        }

        if (action.getFeature().compareTo("purchase") != 0
                && action.getFeature().compareTo("watch") != 0
                    && action.getFeature().compareTo("like") != 0
                        && action.getFeature().compareTo("rate") != 0
                            && action.getFeature().compareTo("subscribe") != 0) {
            return false;
        }

        return true;
    }

    /**
     * The method implements the "purchase" action which is applied to the movie
     * the current user has chosen
     * @param application: the platform's hierarchy of pages that holds
     * the required information
     * @return boolean: "true" if the "purchase" action can happen, "false" otherwise
     */
    public static boolean purchase(final Application application) {
        if (application.getCurrentUser().getCurrentMovieList().size() != 1) {
            return false;
        }

        if (application.getCurrentUser().getPurchasedMovies()
                .contains(application.getCurrentUser().getCurrentMovieList().get(0))) {
            return false;
        }

        if (application.getCurrentUser().getAccountType().compareTo("premium") == 0) {
            if (application.getCurrentUser().getRemainingFreeMovies() >= 1) {
                application.getCurrentUser().payPremiumMovie();
                application.getCurrentUser().addPurchasedMovie(application.getCurrentUser()
                        .getCurrentMovieList().get(0));
                return true;
            }
        }

        if (application.getCurrentUser().getTotalTokens() >= 2) {
            application.getCurrentUser().payMovie();
            application.getCurrentUser().addPurchasedMovie(application.getCurrentUser()
                    .getCurrentMovieList().get(0));
            return true;
        }

        return false;
    }

    /**
     * The method implements the "watch" action which is applied to the movie
     * the current user has chosen
     * @param application: the platform's hierarchy of pages that holds
     * the required information
     * @return boolean: "true" if the "watch" action can happen, "false" otherwise
     */
    public static boolean watch(final Application application) {

        if (application.getCurrentUser().getCurrentMovieList().size() != 1) {
            return false;
        }

        Movie movie = application.getCurrentUser().getCurrentMovieList().get(0);

        if (movie == null) {
            return false;
        }

        if (!application.getCurrentUser().getPurchasedMovies().contains(movie)) {
            return false;
        }

        movie.addView();

        if (!application.getCurrentUser().getWatchedMovies().contains(movie)) {
            application.getCurrentUser().addWatchedMovie(movie);
        }

        return true;
    }

    /**
     * The method implements the "like" action which is applied to the movie
     * the current user has chosen
     * @param application: the platform's hierarchy of pages that holds
     * the required information
     * @return boolean: "true" if the "like" action can happen, "false" otherwise
     */
    public static boolean like(final Application application) {
        if (application.getCurrentUser().getCurrentMovieList().size() != 1) {
            return false;
        }

        Movie movie = application.getCurrentUser().getCurrentMovieList().get(0);

        if (movie == null) {
            return false;
        }

        if (!application.getCurrentUser().getWatchedMovies().contains(movie)) {
            return false;
        }

        if (application.getCurrentUser().getLikedMovies().contains(movie)) {
            return false;
        }

        movie.addLike();
        application.getCurrentUser().addLikedMovie(movie);
        return true;
    }

    /**
     * The method implements the "rate" action which is applied to the movie
     * the current user has chosen
     * @param application: the platform's hierarchy of pages that holds
     * the required information
     * @param action: the movie that is meant to be rated
     * @return boolean: "true" if the "rate" action can happen, "false" otherwise
     */
    public static boolean rate(final Application application, final ActionInput action) {
        if (application.getCurrentUser().getCurrentMovieList().size() != 1) {
            return false;
        }

        if (action.getRate() < 1 || action.getRate() > MAXIMUM_RATE) {
            return false;
        }

        Movie movie = application.getCurrentUser().getCurrentMovieList().get(0);

        if (movie == null) {
            return false;
        }

        if (!application.getCurrentUser().getWatchedMovies().contains(movie)) {
            return false;
        }

        boolean recalculate = true;

        if (!application.getCurrentUser().getRatedMovies().contains(movie)) {
            application.getCurrentUser().addRatedMovie(movie);
            recalculate = false;
        }

        movie.addRating(action.getRate(), recalculate);

        return true;
    }

    /**
     * The method implements the "subscribe" action regarding a given genre
     * @param application: the platform's hierarchy of pages that holds
     * the required information
     * @param action: the chosen genre
     * @return boolean: "true" if the action can happen, "false" otherwise
     */
    public static boolean subscribe(final Application application, final ActionInput action) {
        if (application.getCurrentUser().getCurrentMovieList().size() != 1) {
            return false;
        }

        Movie movie = application.getCurrentUser().getCurrentMovieList().get(0);

        if (movie == null) {
            return false;
        }

        if (!movie.getGenres().contains(action.getSubscribedGenre())) {
            return false;
        }

        if (application.getCurrentUser().getSubscriptions().contains(action.getSubscribedGenre())) {
            return false;
        }

        application.getCurrentUser().addSubscription(action.getSubscribedGenre());
        return true;
    }

    /**
     * The method implements the action of the "See Details Page" if possible
     * @param application: the hierarchy of pages the current one has access to
     * @param action:      the details of the action that is supposed to happen
     * @return boolean: "true" if the action can take place, "false" otherwise
     */
    public static boolean seeDetailsAction(final Application application, final ActionInput action,
                                           final ArrayNode output) {

        if (!canAction(action)) {
            return false;
        }

        if (action.getFeature().compareTo("purchase") == 0) {
            if (purchase(application)) {
                output.add(showOutput(application, action));
                return true;
            }
        }

        if (action.getFeature().compareTo("watch") == 0) {
            if (watch(application)) {
                output.add(showOutput(application, action));
                return true;
            }
        }

        if (action.getFeature().compareTo("like") == 0) {
            if (like(application)) {
                output.add(showOutput(application, action));
                return true;
            }
        }

        if (action.getFeature().compareTo("rate") == 0) {
            if (rate(application, action)) {
                output.add(showOutput(application, action));
                return true;
            }
        }

        if (action.getFeature().compareTo("subscribe") == 0) {
            if (subscribe(application, action)) {
                return true;
            }
        }

        return false;
    }
}
