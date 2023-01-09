package entities;

public final class Notification {
    private String movieName;
    private String message;

    public Notification(final String movieName, final String message) {
        this.movieName = new String(movieName);
        this.message = new String(message);
    }

    public String getMovieName() {
        return movieName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Notification{"
                + "movie name: " + this.movieName + "\n"
                + "message: " + this.message + "}" + "\n";
    }
}
