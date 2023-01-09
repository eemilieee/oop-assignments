package comparators;

import entities.Movie;
import java.util.Comparator;

public final class LikesComparator implements Comparator<Movie> {

    @Override
    public int compare(final Movie movie1, final Movie movie2) {

        if (movie1.getTotalLikes() > movie2.getTotalLikes()) {
            return -1;
        }

        if (movie1.getTotalLikes() < movie2.getTotalLikes()) {
            return 1;
        }

        return 0;
    }
}
