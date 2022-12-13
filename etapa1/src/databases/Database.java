package databases;

import entities.Movie;
import entities.User;
import input.CredentialsInput;
import input.Input;
import input.MovieInput;
import input.UserInput;
import java.util.ArrayList;

public final class Database {
    private static Database instance = null;
    private ArrayList<User> users;
    private ArrayList<Movie> movies;

    private Database() {
        this.users = new ArrayList<User>();
        this.movies = new ArrayList<Movie>();
    }

    /**
     * The method completes the database with the required information
     * @param input: the object that holds the lists of registered
     * users and movies
     */
    public void construct(final Input input) {
        for (UserInput user : input.getUsers()) {
            this.users.add(new User(user.getCredentials()));
        }
        for (MovieInput movie : input.getMovies()) {
            this.movies.add(new Movie(movie));
        }
    }

    /**
     * The method creates a "See Details Page" for each movie of the database
     * @param application: the collection of pages that the added one
     * has access to
     */
    public void init(final Application application) {
        for (Movie movie : this.movies) {
            movie.init(instance, application);
        }
    }

    /**
     * The method returns the Singleton instance of the class
     * @return instance: the Database instance that contains all the information that
     * the implemented platform holds
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }

    public ArrayList<Movie> getMovies() {
        return this.movies;
    }

    /**
     * The method inserts a newly registered user to the platform's database
     * @param credentialsInput: the user's information that is being added
     */
    public void addUser(final CredentialsInput credentialsInput) {
        this.users.add(new User(credentialsInput));
    }

    /**
     * The method resets the database
     * (required action before choosing other sets of users and movies
     * as platform's initial input)
     */
    public void clear() {
        while (!this.users.isEmpty()) {
            this.users.remove(0);
        }

        while (!this.movies.isEmpty()) {
            this.movies.remove(0);
        }
    }
}
