package databases;

import com.fasterxml.jackson.databind.node.ArrayNode;
import entities.Movie;
import entities.User;
import input.*;
import validators.VerifyDatabaseOperations;
import java.util.ArrayList;
import java.util.Observable;

public final class Database extends Observable {
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

        for (User user : this.users) {
            addObserver(user);
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
        addObserver(this.users.get(this.users.size() - 1));
    }

    /**
     * The method checks if a certain movie is present within the database
     * @param movieName: the movie that is being searched for
     * @return boolean: "true" if the movie could be found, "false" otherwise
     */
    private boolean containsMovie(final String movieName) {
        for (Movie movie : this.movies) {
            if (movie.getName().compareTo(movieName) == 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * The method returns a movie from the database based on its title
     * @param movieName: the movie title that is given
     * @return movie: the reference of the movie if it is present within the databse,
     * null otherwise
     */
    public Movie findMovie(final String movieName) {
        for (Movie movie : this.movies) {
            if (movie.getName().compareTo(movieName) == 0) {
                return movie;
            }
        }

        return null;
    }

    /**
     * The method return the position within the database at which a certain movie is found
     * @param movieName: the movie that is searched for
     * @return integer: the requested position (0<= position <movies.size(), if the movie
     * is present in the list, -1 otherwise)
     */
    private int retrieveMovie(final String movieName) {
        int i;
        for (i = 0; i < this.movies.size(); i++) {
            Movie movie = this.movies.get(i);

            if (movie.getName().compareTo(movieName) == 0) {
                return i;
            }
        }

        return -1;
    }

    /**
     * The method adds a new movie to the database and notifies the user about
     * this change
     * @param actionInput: the command that is applied, which contains the information
     * regarding the movie
     * @param output: in case of an error, it is added to the JSON array of outputs
     */
    public void addMovie(final ActionInput actionInput, final ArrayNode output) {

        if (containsMovie(actionInput.getAddedMovie().getName())) {
            output.add(VerifyDatabaseOperations.showActionError());
            return;
        }

        this.movies.add(new Movie(actionInput.getAddedMovie()));

        setChanged();
        notifyObservers(actionInput);
    }

    /**
     * The method removes movie to the database and notifies the user about
     * this change
     * @param actionInput: the command that is applied, which contains the information
     * regarding the movie
     * @param output: in case of an error, it is added to the JSON array of outputs
     */
    public void deleteMovie(final ActionInput actionInput, final ArrayNode output) {

        if (!containsMovie(actionInput.getDeletedMovie())) {
            output.add(VerifyDatabaseOperations.showActionError());
            return;
        }

        int position = retrieveMovie(actionInput.getDeletedMovie());
        this.movies.remove(position);

        setChanged();
        notifyObservers(actionInput);
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

        deleteObservers();

        while (!this.movies.isEmpty()) {
            this.movies.remove(0);
        }
    }
}
