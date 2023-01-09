package comparators;

import entities.Movie;

import java.util.Comparator;

public final class DurationComparator implements Comparator<Movie> {
    private String criteria;

    public DurationComparator(final String criteria) {
        this.criteria = criteria;
    }

    private int increasing(final Movie movie1, final Movie movie2) {
        if (movie1.getDuration() > movie2.getDuration()) {
            return 1;
        }
        if (movie1.getDuration() < movie2.getDuration()) {
            return -1;
        }
        return 0;
    }

    private int decreasing(final Movie movie1, final Movie movie2) {
        if (movie1.getDuration() < movie2.getDuration()) {
            return 1;
        }
        if (movie1.getDuration() > movie2.getDuration()) {
            return -1;
        }
        return 0;
    }


    @Override
    public int compare(final Movie movie1, final Movie movie2) {

        if (this.criteria.compareTo("increasing") == 0) {
            return increasing(movie1, movie2);
        }

        if (this.criteria.compareTo("decreasing") == 0) {
            return decreasing(movie1, movie2);
        }

        return 0;
    }
}
