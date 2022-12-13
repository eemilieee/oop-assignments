package input;

public final class ActionInput {
    private String type;
    private String page;
    private String feature;
    private CredentialsInput credentials;
    private String startsWith;
    private FilterInput filters;
    private String movie;
    private int rate;
    private int count;

    public ActionInput() {
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getPage() {
        return page;
    }

    public void setPage(final String page) {
        this.page = page;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(final String feature) {
        this.feature = feature;
    }

    public CredentialsInput getCredentials() {
        return credentials;
    }

    public void setCredentials(final CredentialsInput credentials) {
        this.credentials = credentials;
    }

    public String getStartsWith() {
        return startsWith;
    }

    public void setStartsWith(final String startsWith) {
        this.startsWith = startsWith;
    }

    public FilterInput getFilters() {
        return filters;
    }

    public void setFilters(final FilterInput filters) {
        this.filters = filters;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(final String movie) {
        this.movie = movie;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(final int rate) {
        this.rate = rate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(final int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Action{"
                + "name: " + this.type + "\n"
                + "password: " + this.page + "\n"
                + "account type: " + this.feature + "\n"
                + "country: " + this.credentials + "\n"
                + "starts with: " + this.startsWith + "\n"
                + "filters: " + this.filters + "\n"
                + "movie: " + this.movie + "\n"
                + "rate: " + this.rate + "\n"
                + "count: " + this.count + "}" + "\n";
    }
}
