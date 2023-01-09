package comparators;

import entities.Genre;
import java.util.Comparator;

public final class GenresComparator implements Comparator<Genre> {

    @Override
    public int compare(final Genre genre1, final Genre genre2) {
        if (genre1.getNoLikes() < genre2.getNoLikes()) {
            return 1;
        }

        if (genre1.getNoLikes() > genre2.getNoLikes()) {
            return -1;
        }

        if (genre1.getNoLikes() == genre2.getNoLikes()) {
            if (genre1.getGenre().compareTo(genre2.getGenre()) > 0) {
                return 1;
            }

            if (genre1.getGenre().compareTo(genre2.getGenre()) < 0) {
                return -1;
            }

            if (genre1.getGenre().compareTo(genre2.getGenre()) == 0) {
                return 0;
            }
        }

        return 0;
    }
}
