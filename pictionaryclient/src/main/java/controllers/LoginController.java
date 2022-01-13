package controllers;

import interfacesControllers.ILoginController;
import interfacesUI.ILogin;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logicModels.Connection;
import util.InputValidator;
import util.Screen;
import util.ScreenLoader;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable, ILogin, ILoginController {

    public TextField tfPassword;
    public TextField tfUsername;
    public Button btnRegister;
    public Button btnLogin;
    private boolean showAlerts = true;

    public void setShowAlerts(boolean showAlerts) {
        this.showAlerts = showAlerts;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection.getInstance().setLogin(this);
    }

    @Override
    public void updateLoginToLobbyOverview() {
        Platform.runLater(() -> {
            Stage loginStage = (Stage) btnLogin.getScene().getWindow();
            ScreenLoader.getInstance().loadScreen(Screen.LOBBYOVERVIEW, loginStage);
            loginStage.close();
        });
    }

    public String getUsernameFromUI() {
        return tfUsername.getText();
    }

    public String getPasswordFromUI() {
        return tfPassword.getText();
    }

    @Override
    public void login(String username, String password) {
        if (InputValidator.validateLoginInput(username, password, showAlerts)) {
            Connection.getInstance().login(username, password);
        } else {
            enableLoginButton();
            throw new IllegalArgumentException("Login is wrong");
        }
    }

    public void btnLoginPressed(ActionEvent actionEvent) {
        btnLogin.setDisable(true);
        String username = getUsernameFromUI();
        String password = getPasswordFromUI();

        if(username.length() == 0 || password.length() == 0) {
            ScreenLoader.getInstance().displayAlert("Error", "Please fill in all fields", AlertType.ERROR);
            btnLogin.setDisable(false);
            return;
        }

        try {
            login(username, password);
        } catch (NullPointerException e) {
            Connection.getInstance().retryConnection();
            if(Connection.getInstance().isConnected()) {
                login(username, password);
            } else {
                enableLoginButton();
                ScreenLoader.getInstance().displayAlert("Error", "Couldn't connect to the server, please check your internet connection", AlertType.ERROR);
            }
        } catch (IllegalArgumentException e) {
            enableLoginButton();
        }
    }

    public void btnRegisterPressed(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            Stage loginStage = (Stage) btnLogin.getScene().getWindow();
            ScreenLoader.getInstance().loadScreen(Screen.REGISTER, loginStage);
            loginStage.close();
        });
    }

    @Override
    public void showError() {
        ScreenLoader.getInstance().displayAlert("Error", "The credentials are invalid. Please try again!", AlertType.ERROR);
    }

    @Override
    public void enableLoginButton() {
        btnLogin.setDisable(false);
    }
}
