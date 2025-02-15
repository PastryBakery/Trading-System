package phase2.trade.widget;

import javafx.scene.control.Label;
import phase2.trade.controller.ControllerResources;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The User widget.
 *
 * @author Dan Lyu
 */
public class UserWidget extends WidgetControllerBase {

    private final Label title = new Label();
    private final Label name = new Label();
    private final Label email = new Label();

    /**
     * Constructs a new User widget.
     *
     * @param controllerResources the controller resources
     */
    public UserWidget(ControllerResources controllerResources) {
        super(controllerResources);
    }

    @Override
    public void refresh() {
        title.setText("UID: " + userToPresent.getUid());
        name.setText("Name: " + userToPresent.getName());
        email.setText("Email: " + userToPresent.getEmail());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGradient("gradient-j");
        addTitle(title);
        addContent(name,email);
        refresh();
    }
}
