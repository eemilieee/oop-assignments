package validators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.ActionInput;

public final class VerifyUnAuth {

    private VerifyUnAuth() {
    }

    /**
     * The method checks if the navigation to a chosen page is possible,
     * according to the platform's rules (from the "Unauthenticated Page")
     * @param action: the action that is supposed to take place
     * @return boolean: "true" if the page switch is possible,
     * "false" otherwise
     */
    public static boolean canNavigate(final ActionInput action) {
        if (action.getType().compareTo("change page") != 0) {
            return false;
        }
        if (action.getPage().compareTo("login") != 0
                                && action.getPage().compareTo("register") != 0) {
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
}
