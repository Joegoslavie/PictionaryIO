package controllers;

import interfacesControllers.IRegisterController;
import interfacesUI.IRegister;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logicModels.Connection;
import util.InputValidator;
import util.Screen;
import util.ScreenLoader;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable, IRegister, IRegisterController {

    public TextField tfEmail;
    public TextField tfUsername;
    public PasswordField pfPassword;
    public PasswordField pfPasswordVerification;
    public Button btnRegister;
    public Button btnCancel;
    public Label lblErrorMessage;
    private boolean showAlerts = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection.getInstance().setRegisterController(this);
        lblErrorMessage.setVisible(false);
    }

    @Override
    public void cancel() {
        updateRegisterToLogin();
    }

    public void setShowAlerts(boolean showAlerts) {
        this.showAlerts = showAlerts;
    }

    public String getUsernameFromUI() {
        return tfUsername.getText();
    }

    public String getPasswordFromUI() {
        return pfPassword.getText();
    }

    public String getPasswordVerificationFromUI() {
        return pfPasswordVerification.getText();
    }

    @Override
    public void register(String username, String password, String passwordVerification) {
        if (InputValidator.validateLoginInput(username, password, showAlerts) && InputValidator.validatePasswordVerification(password, passwordVerification, showAlerts)) {
            Connection.getInstance().register(username, password);
        }
    }

    @Override
    public void updateRegisterToLogin() {
        Platform.runLater(() -> {
            Stage registerStage = (Stage) btnRegister.getScene().getWindow();
            ScreenLoader.getInstance().loadScreen(Screen.LOGIN, registerStage);
            registerStage.close();
        });
    }

    public void btnRegisterPressed(ActionEvent actionEvent) {
        String username = getUsernameFromUI();
        String password = getPasswordFromUI();
        String passwordVerification = getPasswordVerificationFromUI();
        register(username, password, passwordVerification);
    }

    public void btnCancelPressed(ActionEvent actionEvent) {
        cancel();
    }

    @Override
    public void showError(String errorMessage) {
        Platform.runLater(() -> {
            lblErrorMessage.setVisible(true);
            lblErrorMessage.setText(errorMessage);
        });
    }
}
