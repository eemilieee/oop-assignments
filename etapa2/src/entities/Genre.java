package entities;

public final class Genre {
    private String genre;
    private int noLikes;

    public Genre(final String genre) {
        this.genre = new String(genre);
        this.noLikes = 0;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(final String genre) {
        this.genre = genre;
    }

    public int getNoLikes() {
        return noLikes;
    }

    public void setNoLikes(final int noLikes) {
        this.noLikes = noLikes;
    }

    /**
     * The method increases the number of likes for the current genre
     */
    public void addLike() {
        this.noLikes += 1;
    }
}
