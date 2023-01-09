package pages;

import databases.Application;
import databases.Database;

public final class
PageCreator {

    private PageCreator() {
    }

    public enum PageType {
      AuthPage, Login, Logout, Movies, Register, SeeDetails, UnAuthPage, Upgrades;
    }

    /**
     * The method returns a specific type of page the platform has
     * @param pageType: the type of page that is being created
     * @param database: the database that the page manipulates
     * @param application: the collection of pages the newly created one has access to
     * @return page: the instantiated page
     */
    public static Page createPage(final PageType pageType, final Database database,
                                                    final Application application) {

        return switch (pageType) {
            case AuthPage -> new AuthPage(database, application, "Authenticated Page");
            case Login -> new Login(database, application, "Login Page");
            case Logout -> new Logout(database, application, "Logout Page");
            case Movies -> new Movies(database, application, "Movies Page");
            case Register -> new Register(database, application, "Register Page");
            case SeeDetails -> new SeeDetails(database, application, "See Details Page");
            case UnAuthPage -> new UnAuthPage(database, application, "Unauthenticated Page");
            case Upgrades -> new Upgrades(database, application, "Upgrades Page");
        };
    }
}
