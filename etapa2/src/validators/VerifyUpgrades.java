package validators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import databases.Application;
import input.ActionInput;

public final class VerifyUpgrades {

    private static final int TOKENS_FOR_PREMIUM_ACCOUNT = 10;
    private VerifyUpgrades() {
    }

    /**
     * The method checks if the navigation to a chosen page is possible,
     * according to the platform's rules (from the "Upgrades Page")
     * @param action: the action that is supposed to take place
     * @return boolean: "true" if the page switch is possible,
     * "false" otherwise
     */
    public static boolean canNavigate(final ActionInput action) {
        if (action.getType().compareTo("change page") != 0) {
            return false;
        }

        if (action.getPage().compareTo("movies") != 0
            && action.getPage().compareTo("logout") != 0
                && action.getPage().compareTo("homepage") != 0) {
            return false;
        }

        return true;
    }

    /**
     * The method creates the output that is shown if the page action
     * cannot take place
     * @return result: the ObjectNode that will further be added to the
     * JSON ArrayNode that holds the output for the whole application
     */
    public static ObjectNode showActionError() {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();
        ArrayNode movieListOutput = objectMapper.createArrayNode();
        ObjectNode userOutput = null;

        result.put("error", "Error");
        result.set("currentMoviesList", movieListOutput);
        result.set("currentUser", userOutput);

        return result;
    }

    /**
     * The method checks if the "on page" command for the "Upgrades Page" is valid
     * @param action: the details of the action that is supposed to happen
     * @return boolean: "true" if the action can take place, "false" otherwise
     */
    public static boolean canAction(final ActionInput action) {
        if (action.getType().compareTo("on page") != 0) {
            return false;
        }

        if (action.getFeature().compareTo("buy tokens") != 0
                                && action.getFeature().compareTo("buy premium account") != 0) {
            return false;
        }

        return true;
    }

    /**
     * The method implements the "buy tokens" action which is applied to the current user
     * @param application: the platform's hierarchy of pages that holds
     * the required information
     * @return boolean: "true" if the "buy tokens" action can happen, "false" otherwise
     */
    public static boolean buyTokensAction(final Application application,
                                                                    final ActionInput action) {
        if (application.getCurrentUser().getTotalBalance() >= action.getCount()) {
            application.getCurrentUser().buyTokens(action.getCount());
            return true;
        }
        return false;
    }

    /**
     * The method implements the "buy premium account" action which is applied to the current user
     * @param application: the platform's hierarchy of pages that holds
     * the required information
     * @return boolean: "true" if the "buy premium account" action can happen, "false" otherwise
     */
    public static boolean buyPremiumAccountAction(final Application application) {
        if (application.getCurrentUser().getTotalTokens() >= TOKENS_FOR_PREMIUM_ACCOUNT) {
            application.getCurrentUser().buyPremiumAccount();
            return true;
        }
        return false;
    }

    /**
     * The method implements the action of the "Upgrades Page" if possible
     * @param application: the hierarchy of pages the current one has access to
     * @param action: the details of the action that is supposed to happen
     * @return boolean: "true" if the action can take place, "false" otherwise
     */
    public static boolean upgradesAction(final Application application, final ActionInput action) {

        if (!canAction(action)) {
            return false;
        }

        if (action.getFeature().compareTo("buy tokens") == 0) {
            if (buyTokensAction(application, action)) {
                return true;
            }
        }

        if (action.getFeature().compareTo("buy premium account") == 0) {
            if (buyPremiumAccountAction(application)) {
                return true;
            }
        }
        return false;
    }
}
