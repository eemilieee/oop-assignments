package validators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import databases.Application;
import input.ActionInput;

public final class VerifyUndo {

    private VerifyUndo() {
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

    /** The method implements the "back" type of action
     * @param application: the hierarchy of pages
     * @param output: the array of JSON nodes that contains the outputs
     */
    public static void backAction(final Application application, final ArrayNode output) {

        application.setMaxPages(Math.max(application.getMaxPages(),
                application.getPreviousPages().size()));

        if (!application.getPreviousPages().isEmpty()) {

            // erasing the current page from the stack
            application.getPreviousPages().pop();

            // the case when the action can be fulfilled
            if (!application.getPreviousPages().isEmpty()) {

                // retrieving the "change page" action needed to pe performed
                // in order to visit the most recent previous page
                ActionInput previousPage = application.getPreviousPages().peek();

                // the operation of switching pages
                application.getCurrentUser().getCurrentPage().access(application.getCurrentUser(),
                        previousPage, output);
            } else {
                if (application.getMaxPages() != 2) {
                    output.add(showActionError());
                }
            }
        } else {
            // if the stack is empty (the action cannot be completed)
            // or there is no logged-in user at the moment
            output.add(showActionError());
        }
    }
}
