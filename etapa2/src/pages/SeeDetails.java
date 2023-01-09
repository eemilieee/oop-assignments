package pages;

import com.fasterxml.jackson.databind.node.ArrayNode;
import databases.Application;
import databases.Database;
import input.ActionInput;

public final class SeeDetails extends Page {

    public SeeDetails(final Database database, final Application application, final String name) {
        super(database, application, name);
    }

    @Override
    public void action(final PageAccessor pageAccessor, final ActionInput action,
                                                                        final ArrayNode output) {
        pageAccessor.use(this, action, output);
    }

    @Override
    public void access(final PageAccessor pageAccessor, final ActionInput action,
                                                                        final ArrayNode output) {
        pageAccessor.navigateTo(this, this.getApplication(), action, output);
    }
}
