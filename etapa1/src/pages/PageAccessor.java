package pages;

import com.fasterxml.jackson.databind.node.ArrayNode;
import databases.Application;
import input.ActionInput;

public interface PageAccessor {

    /**
     * The method holds the implementation of the page switch
     * @param unAuthPage: the "Unauthenticated Page" from which
     * the navigation starts
     * @param application: the platform that holds the pages that can be accessed
     * @param action: the details of the action
     * @param output: the array of JSON nodes that contains the output
     * of the given action
     */
    void navigateTo(UnAuthPage unAuthPage, Application application, ActionInput action,
                                                                                ArrayNode output);

    /**
     * The method holds the implementation of the page switch
     * @param authPage: the "Authenticated Page" from which
     * the navigation starts
     * @param application: the platform that holds the pages that can be accessed
     * @param action: the details of the action
     * @param output: the array of JSON nodes that contains the output
     * of the given action
     */
    void navigateTo(AuthPage authPage, Application application, ActionInput action,
                                                                                ArrayNode output);

    /**
     * The method holds the implementation of the page switch
     * @param logout: the "Logout Page" from which
     * the navigation starts
     * @param application: the platform that holds the pages that can be accessed
     * @param action: the details of the action
     * @param output: the array of JSON nodes that contains the output
     * of the given action
     */
    void navigateTo(Logout logout, Application application, ActionInput action,
                                                                                ArrayNode output);

    /**
     * The method holds the implementation of the page switch
     * @param login: the "Login Page" from which
     * the navigation starts
     * @param application: the platform that holds the pages that can be accessed
     * @param action: the details of the action
     * @param output: the array of JSON nodes that contains the output
     * of the given action
     */
    void navigateTo(Login login, Application application, ActionInput action,
                                                                                ArrayNode output);

    /**
     * The method holds the implementation of the page switch
     * @param movies: the "Movies Page" from which
     * the navigation starts
     * @param application: the platform that holds the pages that can be accessed
     * @param action: the details of the action
     * @param output: the array of JSON nodes that contains the output
     * of the given action
     */
    void navigateTo(Movies movies, Application application, ActionInput action,
                                                                                ArrayNode output);

    /**
     * The method holds the implementation of the page switch
     * @param register: the "Register Page" from which
     * the navigation starts
     * @param application: the platform that holds the pages that can be accessed
     * @param action: the details of the action
     * @param output: the array of JSON nodes that contains the output
     * of the given action
     */
    void navigateTo(Register register, Application application, ActionInput action,
                                                                                ArrayNode output);

    /**
     * The method holds the implementation of the page switch
     * @param seeDetails: the "See Details Page" from which
     * the navigation starts
     * @param application: the platform that holds the pages that can be accessed
     * @param action: the details of the action
     * @param output: the array of JSON nodes that contains the output
     * of the given action
     */
    void navigateTo(SeeDetails seeDetails, Application application, ActionInput action,
                                                                                ArrayNode output);

    /**
     * The method holds the implementation of the page switch
     * @param upgrades: the "Upgrades Page" from which
     * the navigation starts
     * @param application: the platform that holds the pages that can be accessed
     * @param action: the details of the action
     * @param output: the array of JSON nodes that contains the output
     * of the given action
     */
    void navigateTo(Upgrades upgrades, Application application, ActionInput action,
                                                                                ArrayNode output);

    /**
     * The method holds the implementation of the specific on-page action
     * @param unAuthPage: the "Unauthenticated Page" on which the
     * action takes place
     * @param action: the details of the action
     * @param output: the array of JSON nodes that contains the output
     * of the given action
     */
    void use(UnAuthPage unAuthPage, ActionInput action, ArrayNode output);

    /**
     * The method holds the implementation of the specific on-page action
     * @param authPage: the "Authenticated Page" on which the
     * action takes place
     * @param action: the details of the action
     * @param output: the array of JSON nodes that contains the output
     * of the given action
     */
    void use(AuthPage authPage, ActionInput action, ArrayNode output);

    /**
     * The method holds the implementation of the specific on-page action
     * @param logout: the "Logout Page" on which the
     * action takes place
     * @param action: the details of the action
     * @param output: the array of JSON nodes that contains the output
     * of the given action
     */
    void use(Logout logout, ActionInput action, ArrayNode output);

    /**
     * The method holds the implementation of the specific on-page action
     * @param login: the "Login Page" on which the
     * action takes place
     * @param action: the details of the action
     * @param output: the array of JSON nodes that contains the output
     * of the given action
     */
    void use(Login login, ActionInput action, ArrayNode output);

    /**
     * The method holds the implementation of the specific on-page action
     * @param movies: the "Movies Page" on which the
     * action takes place
     * @param action: the details of the action
     * @param output: the array of JSON nodes that contains the output
     * of the given action
     */
    void use(Movies movies, ActionInput action, ArrayNode output);

    /**
     * The method holds the implementation of the specific on-page action
     * @param register: the "Register Page" on which the
     * action takes place
     * @param action: the details of the action
     * @param output: the array of JSON nodes that contains the output
     * of the given action
     */
    void use(Register register, ActionInput action, ArrayNode output);

    /**
     * The method holds the implementation of the specific on-page action
     * @param seeDetails: the "See Details Page" on which the
     * action takes place
     * @param action: the details of the action
     * @param output: the array of JSON nodes that contains the output
     * of the given action
     */
    void use(SeeDetails seeDetails, ActionInput action, ArrayNode output);

    /**
     * The method holds the implementation of the specific on-page action
     * @param upgrades: the "Upgrades Page" on which the
     * action takes place
     * @param action: the details of the action
     * @param output: the array of JSON nodes that contains the output
     * of the given action
     */
    void use(Upgrades upgrades, ActionInput action, ArrayNode output);
}
