package pages;

import com.fasterxml.jackson.databind.node.ArrayNode;
import databases.Application;
import databases.Database;
import input.ActionInput;

public abstract class Page {
    private Database database;
    private Application application;
    private String name;

    public Page(final Database database, final Application application, final String name) {
        this.database = database;
        this.application = application;
        this.name = new String(name);
    }

    /**
     * The method returns the reference of the platform's database
     * that the page holds
     * @return database: the reference of the database the page makes use of
     */
    public Database getDatabase() {
        return database;
    }

    /**
     * The method returns the reference of the platform
     * that the page holds
     * @return application: the reference of the page hierarchy
     * the current page makes use of
     */
    public Application getApplication() {
        return application;
    }

    /**
     * The method returns the current's page name
     * @return name: the page's descriptive title
     */
    public String getName() {
        return name;
    }

    /**
     * The method indicates a future implementation of specific actions a page fulfills
     * @param pageAccessor: the instance that requests the action
     * @param action: the details of the request
     * @param output: the array of JSON nodes that contains the output
     * of the given action
     */
    public abstract void action(PageAccessor pageAccessor, ActionInput action, ArrayNode output);

    /**
     * The method indicated a future implementation of the navigation between
     * the platform's pages
     * @param pageAccessor: the instance that requests the action
     * @param action: the details of the request
     * @param output: the array of JSON nodes that contains the output
     * of the given action
     */
    public abstract void access(PageAccessor pageAccessor, ActionInput action, ArrayNode output);

    /**
     * The method returns a string that contains the page's information
     * @return string: the contained details that are requested
     */
    @Override
    public String toString() {
        return "the name of the page is " + this.name + "\n";
    }
}
