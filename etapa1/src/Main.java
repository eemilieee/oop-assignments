import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import databases.Application;
import databases.Database;
import entities.User;
import input.ActionInput;
import input.Input;
import java.io.File;
import java.io.IOException;

public final class Main {
    private Main() {
    }

    /**
     * The entry-point of the program
     * @param args: the path to the input JSON file and the one for the output
     * @throws IOException: if the input path is incorrect
     */
    public static void main(final String[] args) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode output = objectMapper.createArrayNode();
        Input inputData = objectMapper.readValue(new File(args[0]), Input.class);

        // creating the required database for the implemented platform
        Database database = Database.getInstance();
        database.clear();
        database.construct(inputData);

        // creating the page hierarchy
        Application application = Application.getInstance();
        application.init(database);
        // creating a "See Details Page" for all the movies that are found within the database
        database.init(application);

        int i;
        for (i = 0; i < inputData.getActions().size(); i++) {
            ActionInput action = inputData.getActions().get(i);

            // if the platform does not have an active user who is
            // navigating through the pages' hierarchy, a default one
            // is created
            if (!application.getHasLoggedInUser()) {
                User defaultUser = new User();
                // redirecting to the "Unauthenticated Page"
                defaultUser.init(application);
                application.setCurrentUser(defaultUser);
            }
            if (action.getType().compareTo("change page") == 0) {
                application.getCurrentUser().getCurrentPage().access(application.getCurrentUser(),
                                                                                action, output);
            }
            if (action.getType().compareTo("on page") == 0) {
                application.getCurrentUser().getCurrentPage().action(application.getCurrentUser(),
                                                                                action, output);
            }
        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(args[1]), output);
    }
}
