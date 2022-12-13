package entities;

import databases.Application;
import databases.Database;
import input.MovieInput;
import pages.SeeDetails;
import java.util.ArrayList;

public final class Movie {
    private String name;
    private int year;
    private int duration;
    private ArrayList<String> genres;
    private ArrayList<String> actors;
    private ArrayList<String> bannedCountries;
    private int totalLikes;
    private double rating;
    private int totalViews;
    private int totalRatings;
    private int noRatings;
    private SeeDetails info;

    public Movie(final MovieInput movieInput) {
        this.name = new String(movieInput.getName());
        this.year = movieInput.getYear();
        this.duration = movieInput.getDuration();
        this.genres = new ArrayList<String>();
        for (String genre : movieInput.getGenres()) {
            this.genres.add(new String(genre));
        }

        this.actors = new ArrayList<String>();
        for (String actor : movieInput.getActors()) {
            this.actors.add(new String(actor));
        }

        this.bannedCountries = new ArrayList<String>();
        for (String bannedCountry : movieInput.getCountriesBanned()) {
            this.bannedCountries.add(new String(bannedCountry));
        }

        this.totalLikes = 0;
        this.rating = 0;
        this.totalViews = 0;
        this.totalRatings = 0;
        this.noRatings = 0;
    }

    /**
     * The method offers access to the current movie to its "See Details Page"
     * by creating it
     * @param database: the platform's database that the page has access to
     * @param application: the collection of pages that the page has
     * hyperlinks to
     */
    public void init(final Database database, final Application application) {
        this.info = new SeeDetails(database, application, "See Details Page");
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public int getDuration() {
        return duration;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    public ArrayList<String> getBannedCountries() {
        return bannedCountries;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(final int rating) {
        this.rating = rating;
    }

    public int getTotalViews() {
        return totalViews;
    }

    public SeeDetails getInfo() {
        return info;
    }

    public int getTotalRatings() {
        return totalRatings;
    }

    public int getNoRatings() {
        return noRatings;
    }

    /**
     * The method counts the total number of likes the movie has received from
     * the platform's users
     */
    public void addLike() {
        this.totalLikes += 1;
    }

    /**
     * The method counts the total number of views the movie has received from
     * the platform's users
     */
    public void addView() {
        this.totalViews += 1;
    }

    /**
     * The method calculates the current movie's rating
     * @param givenRating: the rate that a user offered
     */
    public void addRating(final int givenRating) {
        this.totalRatings = this.totalRatings + givenRating;
        this.noRatings += 1;
        double result = this.totalRatings;
        result /= this.noRatings;
        this.rating = result;
    }

    @Override
    public String toString() {
        return "Movie{"
                + "name: " + this.name + "\n"
                + "year: " + this.year + "\n"
                + "duration: " + this.duration + "\n"
                + "genres: " + this.genres + "\n"
                + "actors: " + this.actors + "\n"
                + "likes: " + this.totalLikes + "\n"
                + "rating: " + this.rating + "\n"
                + "views: " + this.totalViews + "\n"
                + "banned countries: " + this.bannedCountries + "}" + "\n";
    }
}
