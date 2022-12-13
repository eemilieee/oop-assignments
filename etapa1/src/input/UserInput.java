package input;

public final class UserInput {
    private CredentialsInput credentials;

    public CredentialsInput getCredentials() {
        return credentials;
    }

    public void setCredentials(final CredentialsInput credentials) {
        this.credentials = credentials;
    }

    @Override
    public String toString() {
        return "User{"
                + "credentials: " + this.credentials + "}" + "\n";
    }
}
