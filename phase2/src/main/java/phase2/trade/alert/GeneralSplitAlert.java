package phase2.trade.alert;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// this is not the splitpane, but instead two VBoxes in an HBox
// the name may change in the future to avoid confusion
public class GeneralSplitAlert extends AlertWindow<Void> {

    private final HBox root;

    private final VBox right;

    public GeneralSplitAlert(Stage parent, String title, String header) {
        super(parent, title, header);
        body.setSpacing(35);
        root = new HBox(10);
        right = new VBox(35);
        root.getChildren().addAll(body, right);
    }

    public void addLeft(Node... nodes) {
        addNodes(nodes);
    }

    public void addRight(Node... nodes) {
        right.getChildren().addAll(nodes);
    }

    public Void display(String... args) {
        confirmButton.setOnAction(confirmHandler);
        Button cancelButton = new JFXButton("Cancel");

        confirmButton.setOnAction(confirmHandler);
        cancelButton.addEventFilter(ActionEvent.ACTION, event -> {
            alert.hideWithAnimation();
        });
        cancelButton.setFocusTraversable(false);

        body.setMinWidth(250);
        right.setMinWidth(250); // use a configuration file in the future

        alert.setOverlayClose(false);

        layout.getBody().setAll(root);
        layout.setActions(cancelButton, confirmButton);

        alert.setContent(layout);

        alert.showAndWait();
        return null;
    }

}
