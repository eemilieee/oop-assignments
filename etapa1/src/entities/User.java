package entities;

import com.fasterxml.jackson.databind.node.ArrayNode;
import databases.Application;
import input.ActionInput;
import input.CredentialsInput;
import pages.*;
import validators.*;
import java.util.ArrayList;

public final class User implements PageAccessor {
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
    private Page currentPage;
    private static final int DEFAULT_INITIAL_FREE_MOVIES = 15;
    private static final int TOKENS_FOR_PREMIUM_ACCOUNT = 10;

    public User() {
        this.currentMovieList = new ArrayList<Movie>();
        this.purchasedMovies = new ArrayList<Movie>();
        this.watchedMovies = new ArrayList<Movie>();
        this.likedMovies = new ArrayList<Movie>();
        this.ratedMovies = new ArrayList<Movie>();
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
            if (action.getPage().compareTo("logout") == 0) {
                this.currentPage = application.getLogout();
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
            this.currentPage = seeDetails.getApplication().getAuthPage();
        }
    }

    @Override
    public void use(final Upgrades upgrades, final ActionInput action, final ArrayNode output) {
        if (!VerifyUpgrades.upgradesAction(upgrades.getApplication(), action)) {
            output.add(VerifyUpgrades.showActionError());
            this.currentPage = upgrades.getApplication().getAuthPage();
        }
    }
}
