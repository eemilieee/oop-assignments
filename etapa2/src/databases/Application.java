package databases;

import entities.User;
import input.ActionInput;
import pages.*;
import java.util.ArrayList;
import java.util.Stack;

public final class Application {
    private static Application instance = null;
    private final ArrayList<Page> pages;
    private User currentUser;
    private boolean hasLoggedInUser;

    // the stack stores all the "change page" commands in order to determine
    // to which page the user should be redirected to when visiting the most recent
    // previous page
    // the top element of the stack represents the command which belongs to the
    // current page
    private final Stack<ActionInput> previousPages;

    // the attribute memorizes the number of pages that are visited within the
    // application's page hierarchy by the current user
    // (which is similar to a path in a tree data structure)
    private int maxPages;

    private Application() {
        this.pages = new ArrayList<Page>();
        this.previousPages = new Stack<ActionInput>();
    }

    /**
     * The method returns the Singleton instance of the class
     *
     * @return instance: the Application instance that contains all the pages that
     * the implemented platform contains
     */
    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    public UnAuthPage getUnAuthPage() {
        return (UnAuthPage) this.pages.get(PageCreator.PageType.UnAuthPage.ordinal());
    }

    public AuthPage getAuthPage() {
        return (AuthPage) this.pages.get(PageCreator.PageType.AuthPage.ordinal());
    }

    public Logout getLogout() {
        return (Logout) this.pages.get(PageCreator.PageType.Logout.ordinal());
    }

    public Login getLogin() {
        return (Login) this.pages.get(PageCreator.PageType.Login.ordinal());
    }

    public Register getRegister() {
        return (Register) this.pages.get(PageCreator.PageType.Register.ordinal());
    }

    public Movies getMovies() {
        return (Movies) this.pages.get(PageCreator.PageType.Movies.ordinal());
    }

    public Upgrades getUpgrades() {
        return (Upgrades) this.pages.get(PageCreator.PageType.Upgrades.ordinal());
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public boolean getHasLoggedInUser() {
        return this.hasLoggedInUser;
    }

    public int getMaxPages() {
        return maxPages;
    }

    public void setMaxPages(final int maxPages) {
        this.maxPages = maxPages;
    }

    /**
     * The method initializes the attributes the platform has
     *
     * @param database: the database that the contained pages have access to
     */
    public void init(final Database database) {

        for (PageCreator.PageType pageType : PageCreator.PageType.values()) {
            this.pages.add(PageCreator.createPage(pageType, database, instance));
        }

        userIsNotLoggedIn();
        emptyPreviousPages();
    }

    public void setCurrentUser(final User user) {
        this.currentUser = user;
    }

    /**
     * The method indicates that the platform currently has a logged-in user
     */
    public void userIsLoggedIn() {
        this.hasLoggedInUser = true;
    }

    /**
     * The method indicates that the platform does not currently have
     * a logged-in user
     */
    public void userIsNotLoggedIn() {
        this.hasLoggedInUser = false;
    }

    public Stack<ActionInput> getPreviousPages() {
        return previousPages;
    }

    /**
     * The method adds a new "change page" action to the platform's stack of
     * fulfilled commands used for moving onto a previous page within the page's
     * hierarchy
     * @param action: the action that is added to the stack
     */
    public void addPreviousPage(final ActionInput action) {

        if (this.previousPages.isEmpty()) {
            this.previousPages.push(action);
            return;
        }

        if (action.getPage().compareTo(this.previousPages.peek().getPage()) != 0) {
            this.previousPages.push(action);
        }
    }

    /**
     * The method clears the stack of fulfilled commands used for moving onto a previous
     * page within the application
     */
    public void emptyPreviousPages() {
        while (!this.previousPages.isEmpty()) {
            this.previousPages.pop();
        }

        this.maxPages = 0;
    }
}
