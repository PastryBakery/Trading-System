package phase2.trade.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import phase2.trade.callback.ResultStatus;
import phase2.trade.validator.ValidatorBind;
import phase2.trade.validator.ValidatorType;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController extends AbstractController implements Initializable {

    private StringProperty submissionResultProperty;

    public Label submissionResult;

    public TextField username, email, password, country, city;

    public JFXButton registerButton;

    public RegisterController(ControllerResources controllerResources) {
        super(controllerResources);
    }

    public void registerButtonClicked(ActionEvent actionEvent) {
        registerButton.setDisable(true);
        ValidatorBind validatorBind = new ValidatorBind(submissionResultProperty).validate(ValidatorType.USER_NAME, "Invalid UserName", username.getText())
                .validate(ValidatorType.EMAIL, "Invalid Email", email.getText()).validate(ValidatorType.PASSWORD, "Invalid Password", password.getText());
        if (!validatorBind.isAllPass()) {
            registerButton.setDisable(false);
            return;
        }
        submissionResultProperty.setValue("Signing up..");
        getAccountManager().register((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> {
                getSceneManager().switchScene("dashboard.fxml", DashboardController::new);
            });
            resultStatus.setFailed(() -> {
                registerButton.setDisable(false);
                submissionResultProperty.setValue("Username / Email already exists");
            });
            resultStatus.handle();
        }, username.getText(), email.getText(), password.getText(), country.getText(), city.getText());
    }

    public void goToSignIn(ActionEvent actionEvent) {
        getSceneManager().switchScene("login.fxml", LoginController::new);
    }

    public void goToGuest(ActionEvent actionEvent) {
        getAccountManager().loginAsGuest();
        getSceneManager().switchScene("dashboard.fxml", DashboardController::new);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        submissionResultProperty = new SimpleStringProperty("");
        submissionResult.textProperty().bind(submissionResultProperty);
    }
}
