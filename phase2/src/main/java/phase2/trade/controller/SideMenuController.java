package phase2.trade.controller;

import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.ItemListType;
import phase2.trade.presenter.ItemListController;
import phase2.trade.presenter.MarketListController;
import phase2.trade.user.AccountManager;
import phase2.trade.user.RegularUser;
import phase2.trade.view.ConfirmWindow;

import java.net.URL;
import java.util.ResourceBundle;

public class SideMenuController extends AbstractController implements Initializable {

    private final VBox right;
    public JFXListView<Label> sideList;
    public Label userInfo, market, wishList, settings, inventory;
    public VBox userInfoBox;

    public JFXListView<Label> bottomSideList;

    public JFXPanel panel = new JFXPanel();
    private final VBox center;

    private final AccountManager accountManager;

    public SideMenuController(GatewayBundle gatewayBundle, AccountManager accountManager, VBox center, VBox right) {
        super(gatewayBundle);
        this.accountManager = accountManager;
        this.center = center;
        this.right = right;
    }

    // TODO: drop down sub menu
    private void userInfo() {
        Parent userPane = getSceneFactory().loadPane("user_info.fxml", new UserInfoPresenter(accountManager.getLoggedInUser()));
        center.getChildren().clear();
        center.getChildren().addAll(userPane);
    }

    private void market() {
        Parent userPane = getSceneFactory().loadPane("market_list.fxml", new MarketListController(gatewayBundle,(Stage) bottomSideList.getScene().getWindow()));
        center.getChildren().clear();
        center.getChildren().addAll(userPane);
    }

    private void inventory() {
        Parent userPane = getSceneFactory().loadPane("item_list.fxml", new ItemListController(gatewayBundle, accountManager.getLoggedInUser().getItemList(ItemListType.INVENTORY)));
        center.getChildren().clear();
        center.getChildren().addAll(userPane);
    }

    private void wishList() {
        Parent userPane = getSceneFactory().loadPane("add_wish.fxml", new WishItemAddController(gatewayBundle, ((RegularUser) accountManager.getLoggedInUser()), ItemListType.CART));
        center.getChildren().clear();
        center.getChildren().addAll(userPane);
    }

    private void userOperation() {
        Parent userPane = getSceneFactory().loadPane("operation_list.fxml", new UserOperationController(gatewayBundle));
        center.getChildren().clear();
        center.getChildren().addAll(userPane);
    }

    // TODO: make a factory for this and extend for different users
    public void signOut() {
        ConfirmWindow confirmWindow = new ConfirmWindow((Stage) bottomSideList.getScene().getWindow(),"Sign out", "Do you really want to sign out?");
        bottomSideList.getSelectionModel().clearSelection();
        if (confirmWindow.display()) {
            accountManager.logOut();
            getSceneSwitcher().switchScene("login.fxml", new LoginController(gatewayBundle, accountManager));
        } else {
        }
    }

    // make a factory for this
    public void exit() {
        ConfirmWindow confirmWindow = new ConfirmWindow((Stage) bottomSideList.getScene().getWindow(),"Exit", "Do you really want to exit?");
        bottomSideList.getSelectionModel().clearSelection();
        if (confirmWindow.display()) {
            Platform.exit();
        } else {
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeAbstractController(center);
        userInfoBox.getChildren().add(getSceneFactory().loadPane("user_info_side.fxml", new UserInfoPresenter(accountManager.getLoggedInUser())));
        sideList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue.getId()) {
                    case "userInfo":
                        userInfo();
                        break;
                    case "market":
                        market();
                        break;
                    case "inventory":
                        inventory();
                        break;
                    case "wishList":
                        wishList();
                        break;
                    case "userOperation":
                        userOperation();
                        break;
                }
            }
        });

        sideList.getSelectionModel().select(2);
        bottomSideList.setOnMouseClicked(event -> {
                    switch (bottomSideList.getSelectionModel().getSelectedItem().getId()) {
                        case "signOut":
                            signOut();
                            break;
                        case "exit":
                            exit();
                            break;
                    }
                }
        );

    }
}
