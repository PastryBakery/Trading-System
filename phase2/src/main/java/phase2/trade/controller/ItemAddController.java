package phase2.trade.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import phase2.trade.callback.ResultStatus;
import phase2.trade.command.Command;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.command.AddItemToItemList;
import phase2.trade.user.RegularUser;
import phase2.trade.user.User;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ItemAddController implements Initializable {

    public JFXComboBox<Category> category;
    public JFXTextField name;
    public JFXTextArea description;
    public JFXButton submitButton;
    public GridPane root;

    private GatewayBundle gatewayBundle;
    private RegularUser operator;
    private ItemListType itemListType;
    private ObservableList<Item> display;

    private Stage window;

    public ItemAddController(GatewayBundle gatewayBundle, RegularUser user, ItemListType itemListType, ObservableList<Item> display) {
        this.gatewayBundle = gatewayBundle;
        this.operator = user;
        this.itemListType = itemListType;
        this.display = display;
    }

    public void submitItem(ActionEvent actionEvent) {
        submitButton.setDisable(true);
        Command<Item> itemCommand = new AddItemToItemList(gatewayBundle, operator, itemListType);
        itemCommand.execute((result, resultStatus) -> {
            if (resultStatus == ResultStatus.NO_PERMISSION) {

            } else {
                Platform.runLater(() -> {
                    display.add(result);
                    window.close();
                });
            }
        }, name.getText(), description.getText());
        submitButton.setDisable(false);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        category.getItems().addAll(Arrays.asList(Category.values()));
        window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(250);

        Scene scene = new Scene(root);
        window.setScene(scene);
        window.showAndWait();
    }
}
