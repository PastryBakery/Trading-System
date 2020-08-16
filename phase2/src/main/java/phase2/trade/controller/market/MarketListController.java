package phase2.trade.controller.market;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import phase2.trade.command.Command;
import phase2.trade.controller.AbstractListController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.item.Item;
import phase2.trade.item.Willingness;
import phase2.trade.item.command.AddToCart;
import phase2.trade.item.command.GetMarketItems;
import phase2.trade.view.MarketItemCell;
import phase2.trade.view.NoSelectionModel;
import phase2.trade.view.NodeFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

@ControllerProperty(viewFile = "general_list_view.fxml")
public class MarketListController extends AbstractListController<Item> implements Initializable {

    public JFXListView<Item> listView;

    public MarketListController(ControllerResources controllerResources) {
        super(controllerResources, false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        Command<List<Item>> getMarket = getCommandFactory().getCommand(GetMarketItems::new);
        getMarket.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> {
                setDisplayData(result);
                afterFetch();
            });
            resultStatus.handle(getPopupFactory());
        });
    }

    private ComboBox<String> countryCombo;
    private ComboBox<String> provinceCombo;
    private ComboBox<String> cityCombo;

    private void afterFetch() {
        listView.setSelectionModel(new NoSelectionModel<>());
        AddToCart addToCartCommand = getCommandFactory().getCommand(AddToCart::new);
        listView.setCellFactory(param -> new MarketItemCell(addToCartCommand, getPopupFactory()));
        JFXCheckBox lend = new JFXCheckBox("Lend");
        JFXCheckBox sell = new JFXCheckBox("Sell");

        VBox vBox = new VBox(5);
        vBox.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().addAll(lend, sell);

        JFXComboBox<String> category = getNodeFactory().getComboBoxByType(NodeFactory.ComboBoxType.Category);

        JFXToggleButton includeMine = new JFXToggleButton();
        includeMine.setText("includeMine");

        TextField priceMinInclusive = getNodeFactory().getDefaultTextField("Min Price");
        TextField priceMaxInclusive = getNodeFactory().getDefaultTextField("Max Price");
        TextField name = getNodeFactory().getDefaultTextField("Search Name");
        TextField description = getNodeFactory().getDefaultTextField("Search Description");

        getNodeFactory().getAddressComboBoxes((a, b, c) -> {
            countryCombo = a;
            provinceCombo = b;
            cityCombo = c;
        },getConfigBundle().getGeoConfig());

        Pattern doublePattern = Pattern.compile("\\d+\\.?\\d?");
        listViewGenerator.getFilterGroup().addCheckBox(lend, ((entity, toMatch) -> entity.getWillingness() == Willingness.Lend))
                .addCheckBox(sell, ((entity, toMatch) -> entity.getWillingness() == Willingness.Sell))
                .addComboBox(category, (entity, toMatch) -> entity.getCategory().name().equalsIgnoreCase(toMatch))
                .addToggleButton(includeMine, ((entity, toMatch) -> entity.getOwner().getUid().
                        equals(getAccountManager().getLoggedInUser().getUid())))
                .addSearch(priceMinInclusive, ((entity, toMatch) -> {
                    if (!doublePattern.matcher(toMatch).matches()) {
                        getPopupFactory().toast(3, "Please enter a double in Price Min", "CLOSE");
                        return true;
                    } else {
                        return entity.getPrice() >= Double.parseDouble(toMatch);
                    }
                }))
                .addSearch(priceMaxInclusive, ((entity, toMatch) -> {
                    if (!doublePattern.matcher(toMatch).matches()) {
                        getPopupFactory().toast(3, "Please enter a double in Price Max", "CLOSE");
                        return true;
                    } else {
                        return entity.getPrice() <= Double.parseDouble(toMatch);
                    }
                }))
                .addSearch(priceMaxInclusive, ((entity, toMatch) -> {
                    if (!doublePattern.matcher(toMatch).matches()) {
                        getPopupFactory().toast(3, "Please enter a double in Price Max", "CLOSE");
                        return true;
                    } else {
                        return entity.getPrice() <= Double.parseDouble(toMatch);
                    }
                }))
                .addSearch(name, ((entity, toMatch) -> {
                    String lowerCaseFilter = toMatch.toLowerCase();
                    return String.valueOf(entity.getName()).toLowerCase().contains(lowerCaseFilter);
                }))
                .addSearch(description, ((entity, toMatch) -> {
                    String lowerCaseFilter = toMatch.toLowerCase();
                    return String.valueOf(entity.getDescription()).toLowerCase().contains(lowerCaseFilter);
                }))
                .addComboBox(provinceCombo, (entity, toMatch) -> entity.getOwner().getAddressBook().getSelectedAddress().getTerritory().equalsIgnoreCase(toMatch));

        lend.setSelected(true);
        sell.setSelected(true);
        includeMine.setSelected(true);
        priceMinInclusive.setMaxWidth(80);
        priceMaxInclusive.setMaxWidth(80);
        Label label = new Label("-");

        getPane("topBar").getChildren().setAll(vBox, name, description, category, includeMine, priceMinInclusive, label, priceMaxInclusive,
                countryCombo, provinceCombo, cityCombo);

        listViewGenerator.build();
    }
}
