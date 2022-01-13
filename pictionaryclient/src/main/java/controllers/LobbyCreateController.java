package controllers;

import com.google.gson.Gson;
import enums.Header;
import interfacesControllers.ILobbyCreateController;
import interfacesUI.ILobbyCreate;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logicModels.Connection;
import models.Lobby;
import models.LobbyShell;
import util.Screen;
import util.ScreenLoader;
import webSocketMessages.WebSocketMessage;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyCreateController implements Initializable, ILobbyCreate, ILobbyCreateController {

    public TextField tfLobbyName;
    public ComboBox cbAccessibility;
    public ComboBox cbPlayerAmount;
    public ComboBox cbAmountOfTurnsPerPlayer;
    public Button btnCreateLobby;
    private boolean showAlerts;

    public void setShowAlerts(boolean showAlerts){
        this.showAlerts = showAlerts;
    }

    private Gson gson = new Gson();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfLobbyName.setText("Name");

        Connection.getInstance().setLobbyCreateController(this);

        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Public",
                        "Private"
                );

        cbAccessibility.setItems(options);

        options =
                FXCollections.observableArrayList(
                        "1",
                        "2",
                        "3",
                        "4",
                        "5"
                );

        cbAmountOfTurnsPerPlayer.setItems(options);

        options =
                FXCollections.observableArrayList(
                        "2",
                        "3",
                        "4",
                        "5"
                );

        cbPlayerAmount.setItems(options);
    }

    @Override
    public void createLobby(String lobbyName, boolean isPrivate, int maxPlayerAmount, int roundAmount) {
        LobbyShell lobbyShell = new LobbyShell(maxPlayerAmount, lobbyName, isPrivate, roundAmount);

        WebSocketMessage message = new WebSocketMessage(Header.LOBBYCREATE, gson.toJson(lobbyShell));

        Connection connection = Connection.getInstance();
        connection.sendMessage(message);
    }
    
    public void createLobbyButtonClick(ActionEvent actionEvent) {
        if(!checkInput()) {
            showErrorDialog("It appears that not all fields have been filled in correctly. Please make sure everything is the way it should be.");
            return;
        }

        int amountOfPlayers = Integer.valueOf(cbPlayerAmount.getValue().toString());
        String name = tfLobbyName.getText();
        boolean privateLobby = false;
        if (cbAccessibility.getValue().toString().equalsIgnoreCase("private")) {
            privateLobby = true;
        }
        int id = 0;
        int amountOfRounds = Integer.valueOf(cbAmountOfTurnsPerPlayer.getValue().toString());
        createLobby(name, privateLobby, amountOfPlayers, amountOfRounds);
    }

    private boolean checkInput() {
        if(tfLobbyName.getText().length() < 4) {
            return false;
        }

        if(!cbAccessibility.getValue().toString().equalsIgnoreCase("private") && !cbAccessibility.getValue().toString().equalsIgnoreCase("public")) {
            return false;
        }

        return true;
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    @Override
    public void updateLobbyCreated(boolean succes, Lobby lobby) {
        if(succes) {
            Platform.runLater(() -> {
                Stage lobbyCreateStage = (Stage)btnCreateLobby.getScene().getWindow();
                ScreenLoader.getInstance().loadScreen(Screen.LOBBY, lobby, lobbyCreateStage);
                lobbyCreateStage.close();
            });
        }
    }

    public void btnCancelClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage)btnCreateLobby.getScene().getWindow();
        ScreenLoader.getInstance().loadScreen(Screen.LOBBYOVERVIEW);
        currentStage.close();
    }
}
