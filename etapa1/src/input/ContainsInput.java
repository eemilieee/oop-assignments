package input;

import java.util.ArrayList;

public final class ContainsInput {
    private ArrayList<String> actors;
    private ArrayList<String> genre;

    public ArrayList<String> getActors() {
        return actors;
    }

    public void setActors(final ArrayList<String> actors) {
        this.actors = actors;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }

    public void setGenres(final ArrayList<String> genres) {
        this.genre = genres;
    }

    @Override
    public String toString() {
        return "Contains{"
                + "actors: " + this.actors + "\n"
                + "genre: " + this.genre + "}" + "\n";
    }
}
