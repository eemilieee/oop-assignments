package databases;

import entities.User;
import pages.*;

public final class Application {
    private static Application instance = null;
    private UnAuthPage unAuthPage;
    private AuthPage authPage;
    private Logout logout;
    private Login login;
    private Register register;
    private Movies movies;
    private Upgrades upgrades;
    private User currentUser;
    private boolean hasLoggedInUser;

    private Application() {
    }

    /**
     * The method returns the Singleton instance of the class
     * @return instance: the Application instance that contains all the pages that
     * the implemented platform contains
     */
    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    public UnAuthPage getUnAuthPage() {
        return this.unAuthPage;
    }

    public AuthPage getAuthPage() {
        return this.authPage;
    }

    public Logout getLogout() {
        return this.logout;
    }

    public Login getLogin() {
        return this.login;
    }

    public Register getRegister() {
        return this.register;
    }

    public Movies getMovies() {
        return this.movies;
    }

    public Upgrades getUpgrades() {
        return this.upgrades;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public boolean getHasLoggedInUser() {
        return this.hasLoggedInUser;
    }

    /**
     * The method creates a new "Unauthenticated Page" that the platform has
     * @param database: the database that the page has access to
     * @param name: the name that the page holds
     */
    public void setUnAuthPage(final Database database, final String name) {
        this.unAuthPage = new UnAuthPage(database, instance, name);
    }

    /**
     * The method creates a new "Authenticated Page" that the platform has
     * @param database: the database that the page has access to
     * @param name: the name that the page holds
     */
    public void setAuthPage(final Database database, final String name) {
        this.authPage = new AuthPage(database, instance, name);
    }

    /**
     * The method creates a new "Logout Page" that the platform has
     * @param database: the database that the page has access to
     * @param name: the name that the page holds
     */
    public void setLogout(final Database database, final String name) {
        this.logout = new Logout(database, instance, name);
    }

    /**
     * The method creates a new "Login Page" that the platform has
     * @param database: the database that the page has access to
     * @param name: the name that the page holds
     */
    public void setLogin(final Database database, final String name) {
        this.login = new Login(database, instance, name);
    }

    /**
     * The method creates a new "Register Page" that the platform has
     * @param database: the database that the page has access to
     * @param name: the name that the page holds
     */
    public void setRegister(final Database database, final String name) {
        this.register = new Register(database, instance, name);
    }

    /**
     * The method creates a new "Movies Page" that the platform has
     * @param database: the database that the page has access to
     * @param name: the name that the page holds
     */
    public void setMovies(final Database database, final String name) {
        this.movies = new Movies(database, instance, name);
    }

    /**
     * The method creates a new "Upgrades Page" that the platform has
     * @param database: the database that the page has access to
     * @param name: the name that the page holds
     */
    public void setUpgrades(final Database database, final String name) {
        this.upgrades = new Upgrades(database, instance, name);
    }

    /**
     * The method initializes the attributes the platform has
     * @param database: the database that the contained pages have access to
     */

    public void init(final Database database) {
        setUnAuthPage(database, "Unauthenticated Page");
        setAuthPage(database, "Authenticated Page");
        setLogout(database, "Logout Page");
        setLogin(database, "Login Page");
        setRegister(database, "Register Page");
        setMovies(database, "Movies Page");
        setUpgrades(database, "Upgrades Page");
        userIsNotLoggedIn();
    }

    public void setCurrentUser(final User user) {
        this.currentUser = user;
    }

    /**
     * The method indicates that the platform currently has a logged-in user
     */
    public void userIsLoggedIn() {
        this.hasLoggedInUser = true;
    }

    /**
     * The method indicates that the platform does not currently have
     * a logged-in user
     */
    public void userIsNotLoggedIn() {
        this.hasLoggedInUser = false;
    }
}
