package phase2.trade.controller;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SideMenuController extends AbstractController implements Initializable {

    public JFXListView<Label> sideList;
    public Label userInfo, market, wishList, settings, logOut;
    public VBox userInfoBox;

    private GridPane center;

    private final AccountManager accountManager;

    public SideMenuController(AccountManager accountManager, GridPane center) {
        this.accountManager = accountManager;
        this.center = center;
    }

    private void logOut() {
        accountManager.logOut();
        switchScene("login.fxml", new LoginController(accountManager), logOut);
    }

    private void userInfo() {
        Parent userPane = new UserInfoPresenter(accountManager.getLoggedInUser()).getPane();
        GridPane.setConstraints(userPane, 0, 0);
        center.getChildren().clear();
        center.getChildren().addAll(userPane);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userInfoBox.getChildren().add(loadPane("user_info.fxml", new UserInfoPresenter(accountManager.getLoggedInUser())));
        sideList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue.getId()) {
                    case "userInfo":
                        userInfo();
                        break;
                    case "logOut":
                        logOut();
                        break;
                }
            }
        });
    }
}
