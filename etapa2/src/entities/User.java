package entities;

import com.fasterxml.jackson.databind.node.ArrayNode;
import databases.Application;
import databases.Database;
import input.ActionInput;
import input.CredentialsInput;
import pages.*;
import validators.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public final class User implements PageAccessor, Observer {
    private String name;
    private String password;
    private String accountType;
    private String country;
    private int totalBalance;
    private int totalTokens;
    private int remainingFreeMovies;
    private ArrayList<Movie> currentMovieList;
    private ArrayList<Movie> purchasedMovies;
    private ArrayList<Movie> watchedMovies;
    private ArrayList<Movie> likedMovies;
    private ArrayList<Movie> ratedMovies;
    private ArrayList<Notification> notifications;
    private ArrayList<String> subscriptions;
    private Page currentPage;
    private static final int DEFAULT_INITIAL_FREE_MOVIES = 15;
    private static final int TOKENS_FOR_PREMIUM_ACCOUNT = 10;

    public User() {
        this.currentMovieList = new ArrayList<Movie>();
        this.purchasedMovies = new ArrayList<Movie>();
        this.watchedMovies = new ArrayList<Movie>();
        this.likedMovies = new ArrayList<Movie>();
        this.ratedMovies = new ArrayList<Movie>();
        this.notifications = new ArrayList<Notification>();
        this.subscriptions = new ArrayList<String>();
    }

    public User(final CredentialsInput credentialsInput) {
        this.name = new String(credentialsInput.getName());
        this.password = new String(credentialsInput.getPassword());
        this.accountType = new String(credentialsInput.getAccountType());
        this.country = new String(credentialsInput.getCountry());
        this.totalBalance = credentialsInput.getBalance();
        this.remainingFreeMovies = DEFAULT_INITIAL_FREE_MOVIES;
        this.currentMovieList = new ArrayList<Movie>();
        this.purchasedMovies = new ArrayList<Movie>();
        this.watchedMovies = new ArrayList<Movie>();
        this.likedMovies = new ArrayList<Movie>();
        this.ratedMovies = new ArrayList<Movie>();
        this.notifications = new ArrayList<Notification>();
        this.subscriptions = new ArrayList<String>();
    }

    /**
     * The method sets the base page of the platform where users who are not
     * logged-in are redirected to
     * @param application: the hierarchy of pages that holds
     * the "Unauthenticated Page"
     */
    public void init(final Application application) {
        this.currentPage = application.getUnAuthPage();
    }

    /**
     * The method redirects the logged-in current user to
     * the "Authenticated Page"
     * @param application: the platform the user accesses
     */
    public void auth(final Application application) {
        this.currentPage = application.getAuthPage();
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getCountry() {
        return country;
    }

    public int getTotalBalance() {
        return totalBalance;
    }

    public int getTotalTokens() {
        return totalTokens;
    }

    public int getRemainingFreeMovies() {
        return remainingFreeMovies;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public ArrayList<String> getSubscriptions() {
        return subscriptions;
    }

    public ArrayList<Movie> getCurrentMovieList() {
        return currentMovieList;
    }

    /**
     * The method adds a movie to the list of movies the current user is able
     * to see on the screen
     * @param movie: the movie that is available
     * (can be watched in the country the user comes from)
     */
    public void addCurrentMovie(final Movie movie) {
        this.currentMovieList.add(movie);
    }

    /**
     * The method adds a movie to the list of purchased ones
     * @param movie: the movie that is available
     * (can be watched in the country the user comes from)
     */
    public void addPurchasedMovie(final Movie movie) {
        this.purchasedMovies.add(movie);
    }

    /**
     * The method adds a movie to the list of watched ones
     * @param movie: the movie that is available
     * (can be watched in the country the user comes from)
     */
    public void addWatchedMovie(final Movie movie) {
        this.watchedMovies.add(movie);
    }

    /**
     * The method adds a movie to the list of liked ones
     * @param movie: the movie that is available
     * (can be watched in the country the user comes from)
     */
    public void addLikedMovie(final Movie movie) {
        this.likedMovies.add(movie);
    }

    /**
     * The method adds a movie to the list of rated ones
     * @param movie: the movie that is available
     * (can be watched in the country the user comes from)
     */
    public void addRatedMovie(final Movie movie) {
        this.ratedMovies.add(movie);
    }

    /** The method adds a new subscription to the user
     * @param genre: the genre that the subscription refers to
     */
    public void addSubscription(final String genre) {
        this.subscriptions.add(genre);
    }

    /**
     * The method checks if the current user has already been subscribed to the given genre
     * @param genres: the genre that is searched for within the list if subscriptions
     * @return boolean: "true" if the user is already subscribed, "false" otherwise
     */
    public boolean hasSubscription(final ArrayList<String> genres) {

        for (String genre : this.subscriptions) {
            if (genres.contains(genre)) {
                return true;
            }
        }

        return false;
    }

    /**
     * The method adds a new notification to the notifications' queue
     * @param notification: the notification that is being added
     */
    public void addNotification(final Notification notification) {
        this.notifications.add(notification);
    }

    /**
     * The method removes a given movie from the purchased movies' list
     * @param movieName: the movie that should be deleted
     * @return boolean: "true" if the movie was successfully removed; "false" otherwise
     */
    public boolean removePurchasedMovie(final String movieName) {

        int i, position = -1;

        if (this.purchasedMovies.isEmpty()) {
            return false;
        }

        for (i = 0; i < this.purchasedMovies.size(); i++) {
            Movie movie = this.purchasedMovies.get(i);

            if (movie.getName().compareTo(movieName) == 0) {
                position = i;
            }
        }

        if (position >= 0) {
            this.purchasedMovies.remove(position);
            return true;
        }

        return false;
    }

    /**
     * The method removes a given movie from the watched movies' list
     * @param movieName: the movie that should be deleted
     */
    public void removeWatchedMovie(final String movieName) {

        int i, position = -1;

        if (this.watchedMovies.isEmpty()) {
            return;
        }

        for (i = 0; i < this.watchedMovies.size(); i++) {
            Movie movie = this.watchedMovies.get(i);

            if (movie.getName().compareTo(movieName) == 0) {
                position = i;
            }
        }

        if (position >= 0) {
            this.watchedMovies.remove(position);
        }
    }

    /**
     * The method removes a given movie from the liked movies' list
     * @param movieName: the movie that should be deleted
     */
    public void removeLikedMovie(final String movieName) {

        int i, position = -1;

        if (this.likedMovies.isEmpty()) {
            return;
        }

        for (i = 0; i < this.likedMovies.size(); i++) {
            Movie movie = this.likedMovies.get(i);

            if (movie.getName().compareTo(movieName) == 0) {
                position = i;
            }
        }

        if (position >= 0) {
            this.likedMovies.remove(position);
        }
    }

    /**
     * The method removes a given movie from the rated movies' list
     * @param movieName: the movie that should be deleted
     */
    public void removeRatedMovie(final String movieName) {

        int i, position = -1;

        if (this.ratedMovies.isEmpty()) {
            return;
        }

        for (i = 0; i < this.ratedMovies.size(); i++) {
            Movie movie = this.ratedMovies.get(i);

            if (movie.getName().compareTo(movieName) == 0) {
                position = i;
            }
        }

        if (position >= 0) {
            this.ratedMovies.remove(position);
        }
    }

    /**
     * The method clears the list of movies that are shown on the screen
     */
    public void removeCurrentMovies() {
        while (!this.currentMovieList.isEmpty()) {
            this.currentMovieList.remove(0);
        }
    }

    /**
     * The method fulfills the "purchase" action of a movie
     */
    public void payMovie() {
        this.totalTokens -= 2;
    }

    /**
     * The method fulfills the "purchase" action of a movie
     * if the user is a premium one
     */
    public void payPremiumMovie() {
        this.remainingFreeMovies -= 1;
    }

    /**
     * The method fulfills the action of buying the platform's
     * specific currency (tokes used for purchasing movies
     * and premium accounts)
     */
    public void buyTokens(final int noTokens) {
        this.totalTokens += noTokens;
        this.totalBalance -= noTokens;
    }

    /**
     * The method switches the user's account type to a premium one
     */
    public void buyPremiumAccount() {
        this.totalTokens -= TOKENS_FOR_PREMIUM_ACCOUNT;
        this.accountType = "premium";
    }

    public ArrayList<Movie> getPurchasedMovies() {
        return purchasedMovies;
    }

    public ArrayList<Movie> getWatchedMovies() {
        return watchedMovies;
    }

    public ArrayList<Movie> getLikedMovies() {
        return likedMovies;
    }

    public ArrayList<Movie> getRatedMovies() {
        return ratedMovies;
    }

    public Page getCurrentPage() {
        return this.currentPage;
    }

    @Override
    public String toString() {
        return "Credentials{"
                + "name: " + this.name + "\n"
                + "password: " + this.password + "\n"
                + "account type: " + this.accountType + "\n"
                + "country: " + this.country + "\n"
                + "remaining free movies: " + this.remainingFreeMovies + "\n"
                + "current movie list: " + this.currentMovieList + "\n"
                + "purchased movie list: " + this.purchasedMovies + "\n"
                + "watched movie list: " + this.watchedMovies + "\n"
                + "liked movie list: " + this.likedMovies + "\n"
                + "rated movie list: " + this.ratedMovies + "\n"
                + "subscription: " + this.subscriptions + "\n"
                + "balance: " + this.totalBalance + "}" + "\n";
    }

    @Override
    public void navigateTo(final UnAuthPage unAuthPage, final Application application,
                           final ActionInput action, final ArrayNode output) {

        if (VerifyUnAuth.canNavigate(action)) {
            if (action.getPage().compareTo("login") == 0) {
                this.currentPage = application.getLogin();
                application.userIsLoggedIn();
            }
            if (action.getPage().compareTo("register") == 0) {
                this.currentPage = application.getRegister();
                application.userIsLoggedIn();
            }
        } else {
            output.add(VerifyUnAuth.showActionError());
        }
    }

    @Override
    public void navigateTo(final AuthPage authPage, final Application application,
                           final ActionInput action, final ArrayNode output) {

        if (VerifyAuth.canNavigate(action)) {

            application.addPreviousPage(action);

            if (action.getPage().compareTo("logout") == 0) {
                this.currentPage = application.getLogout();
                application.emptyPreviousPages();
                application.userIsNotLoggedIn();
            }

            if (action.getPage().compareTo("movies") == 0) {
                this.currentPage = application.getMovies();
                output.add(VerifyMovies.showOutput(application, authPage.getDatabase()));
            }

            if (action.getPage().compareTo("upgrades") == 0) {
                this.currentPage = application.getUpgrades();
            }
        } else {
            output.add(VerifyAuth.showActionError());
        }
    }

    @Override
    public void navigateTo(final Logout logout, final Application application,
                           final ActionInput action, final ArrayNode output) {

        this.currentPage = application.getUnAuthPage();
        application.emptyPreviousPages();
        application.userIsNotLoggedIn();
    }

    @Override
    public void navigateTo(final Login login, final Application application,
                           final ActionInput action, final ArrayNode output) {
        output.add(VerifyLogin.showActionError());
    }

    @Override
    public void navigateTo(final Movies movies, final Application application,
                           final ActionInput action, final ArrayNode output) {

        if (VerifyMovies.canNavigate(application, action)) {

            application.addPreviousPage(action);

            if (action.getPage().compareTo("homepage") == 0) {
                this.currentPage = application.getAuthPage();
            }

            if (action.getPage().compareTo("upgrades") == 0) {
                this.currentPage = application.getUpgrades();
            }

            if (action.getPage().compareTo("see details") == 0) {
                if (VerifyMovies.retrieveMovie(application, action) != null) {
                    this.currentPage = VerifyMovies.retrieveMovie(application, action).getInfo();
                    output.add(VerifySeeDetails.showOutput(application, action));
                }
            }
            if (action.getPage().compareTo("logout") == 0) {
                this.currentPage = application.getLogout();
                application.userIsNotLoggedIn();
            }
        } else {
            output.add(VerifyMovies.showActionError());
        }
    }

    @Override
    public void navigateTo(final Register register, final Application application,
                           final ActionInput action, final ArrayNode output) {
        output.add(VerifyRegister.showActionError());
    }

    @Override
    public void navigateTo(final SeeDetails seeDetails, final Application application,
                           final ActionInput action, final ArrayNode output) {

        if (VerifySeeDetails.canNavigate(action)) {

            application.addPreviousPage(action);

            if (action.getPage().compareTo("movies") == 0) {
                this.currentPage = application.getMovies();
                output.add(VerifyMovies.showOutput(application, seeDetails.getDatabase()));
            }

            if (action.getPage().compareTo("upgrades") == 0) {
                this.currentPage = application.getUpgrades();
            }

            if (action.getPage().compareTo("logout") == 0) {
                application.userIsNotLoggedIn();
                this.currentPage = application.getLogout();
            }
        } else {
            output.add(VerifySeeDetails.showActionError());
            this.currentPage = application.getAuthPage();
        }
    }

    @Override
    public void navigateTo(final Upgrades upgrades, final Application application,
                           final ActionInput action, final ArrayNode output) {

        if (VerifyUpgrades.canNavigate(action)) {

            application.addPreviousPage(action);

            if (action.getPage().compareTo("homepage") == 0) {
                this.currentPage = application.getAuthPage();
            }

            if (action.getPage().compareTo("movies") == 0) {
                this.currentPage = application.getMovies();
                output.add(VerifyMovies.showOutput(application, upgrades.getDatabase()));
            }

            if (action.getPage().compareTo("logout") == 0) {
                this.currentPage = application.getLogout();
                application.userIsNotLoggedIn();
            }
        } else {
            output.add(VerifyUpgrades.showActionError());
            this.currentPage = application.getAuthPage();
        }
    }

    @Override
    public void use(final UnAuthPage unAuthPage, final ActionInput action,
                    final ArrayNode output) {
        output.add(VerifyUnAuth.showActionError());
    }

    @Override
    public void use(final AuthPage authPage, final ActionInput action, final ArrayNode output) {
        output.add(VerifyAuth.showActionError());
    }

    @Override
    public void use(final Logout logout, final ActionInput action, final ArrayNode output) {
        this.currentPage = logout.getApplication().getUnAuthPage();
        logout.getApplication().userIsNotLoggedIn();
    }

    @Override
    public void use(final Login login, final ActionInput action, final ArrayNode output) {
        if (VerifyLogin.loginAction(login.getDatabase(), login.getApplication(), action)) {

            ActionInput completedAction = new ActionInput();
            completedAction.setType("change page");
            completedAction.setPage("homepage");

            login.getApplication().addPreviousPage(completedAction);

            output.add(VerifyLogin.showOutput(login.getApplication()));
        } else {
            output.add(VerifyLogin.showError(login.getApplication()));
        }
    }

    @Override
    public void use(final Movies movies, final ActionInput action, final ArrayNode output) {

        if (!VerifyMovies.moviesAction(movies.getDatabase(), movies.getApplication(),
                action, output)) {
            this.currentPage = movies.getApplication().getAuthPage();
            output.add(VerifyMovies.showActionError());
        }
    }

    @Override
    public void use(final Register register, final ActionInput action, final ArrayNode output) {
        if (VerifyRegister.registerAction(register.getDatabase(), register.getApplication(),
                action)) {

            ActionInput completedAction = new ActionInput();
            completedAction.setType("change page");
            completedAction.setPage("homepage");

            register.getApplication().addPreviousPage(completedAction);

            output.add(VerifyRegister.showOutput(register.getApplication()));
        } else {
            output.add(VerifyRegister.showError(register.getApplication()));
        }
    }

    @Override
    public void use(final SeeDetails seeDetails, final ActionInput action,
                    final ArrayNode output) {
        if (!VerifySeeDetails.seeDetailsAction(seeDetails.getApplication(), action, output)) {
            output.add(VerifySeeDetails.showActionError());
        }
    }

    @Override
    public void use(final Upgrades upgrades, final ActionInput action, final ArrayNode output) {
        if (!VerifyUpgrades.upgradesAction(upgrades.getApplication(), action)) {
            output.add(VerifyUpgrades.showActionError());
            this.currentPage = upgrades.getApplication().getAuthPage();
        }
    }

    @Override
    public void update(final Observable observable, final Object arg) {
        ActionInput action = (ActionInput) (arg);
        Database database = (Database) (observable);

        if (action.getFeature().compareTo("add") == 0) {
            Movie addedMovie = database.getMovies().get(database.getMovies().size() - 1);
            if (addedMovie.getBannedCountries().contains(this.country)) {
                return;
            }

            if (hasSubscription(addedMovie.getGenres())) {
                Notification newNotification = new Notification(addedMovie.getName(), "ADD");
                addNotification(newNotification);
            }

            return;
        }

        if (action.getFeature().compareTo("delete") == 0) {

            Movie deletedMovie = database.findMovie(action.getDeletedMovie());

            if (deletedMovie != null && hasSubscription(deletedMovie.getGenres())) {
                Notification newNotification = new Notification(action.getDeletedMovie(), "DELETE");
                addNotification(newNotification);
            }

            boolean didPurchaseMovie = removePurchasedMovie(action.getDeletedMovie());
            removeWatchedMovie(action.getDeletedMovie());
            removeLikedMovie(action.getDeletedMovie());
            removeRatedMovie(action.getDeletedMovie());

            // giving back the resources used for purchasing the movie
            if (this.accountType != null && didPurchaseMovie) {
                if (this.accountType.compareTo("premium") == 0) {
                    this.remainingFreeMovies += 1;
                } else {
                    this.totalTokens += 2;
                }
            }
        }
    }
}
