package comparators;

import entities.Movie;

import java.util.Comparator;

public final class FiltersComparator implements Comparator<Movie> {
    private String rating;
    private String duration;

    public FiltersComparator(final String rating, final String duration) {
        this.rating = rating;
        this.duration = duration;
    }

    private int increasing(final Movie movie1, final Movie movie2) {
        if (movie1.getDuration() > movie2.getDuration()) {
            return 1;
        }
        if (movie1.getDuration() < movie2.getDuration()) {
            return -1;
        }
        if (movie1.getDuration() == movie2.getDuration()) {
            if (movie1.getRating() > movie2.getRating()) {
                return 1;
            }
            if (movie1.getRating() < movie2.getRating()) {
                return -1;
            }
            if (movie1.getRating() == movie2.getRating()) {
                return 0;
            }
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
        if (movie1.getDuration() == movie2.getDuration()) {
            if (movie1.getRating() < movie2.getRating()) {
                return 1;
            }
            if (movie1.getRating() > movie2.getRating()) {
                return -1;
            }
            if (movie1.getRating() == movie2.getRating()) {
                return 0;
            }
        }
        return 0;
    }

    private int increasingDecreasing(final Movie movie1, final Movie movie2) {
        if (movie1.getDuration() < movie2.getDuration()) {
            return 1;
        }
        if (movie1.getDuration() > movie2.getDuration()) {
            return -1;
        }
        if (movie1.getDuration() == movie2.getDuration()) {
            if (movie1.getRating() > movie2.getRating()) {
                return 1;
            }
            if (movie1.getRating() < movie2.getRating()) {
                return -1;
            }
            if (movie1.getRating() == movie2.getRating()) {
                return 0;
            }
        }
        return 0;
    }

    private int decreasingIncreasing(final Movie movie1, final Movie movie2) {
        if (movie1.getDuration() > movie2.getDuration()) {
            return 1;
        }
        if (movie1.getDuration() < movie2.getDuration()) {
            return -1;
        }
        if (movie1.getDuration() == movie2.getDuration()) {
            if (movie1.getRating() < movie2.getRating()) {
                return 1;
            }
            if (movie1.getRating() > movie2.getRating()) {
                return -1;
            }
            if (movie1.getRating() == movie2.getRating()) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public int compare(final Movie movie1, final Movie movie2) {

        if (this.rating.compareTo("increasing") == 0
                && this.duration.compareTo("increasing") == 0) {
            return increasing(movie1, movie2);
        }

        if (this.rating.compareTo("decreasing") == 0
                && this.duration.compareTo("increasing") == 0) {
            return decreasingIncreasing(movie1, movie2);
        }

        if (this.rating.compareTo("increasing") == 0
                && this.duration.compareTo("decreasing") == 0) {
            return increasingDecreasing(movie1, movie2);
        }

        if (this.rating.compareTo("decreasing") == 0
                && this.duration.compareTo("decreasing") == 0) {
            return decreasing(movie1, movie2);
        }

        return 0;
    }
}
