package controllers;

import enums.Header;
import interfacesControllers.ILobbyOverviewController;
import interfacesUI.ILobbyOverview;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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
import java.util.Timer;
import java.util.TimerTask;

public class LobbyOverviewController implements Initializable, ILobbyOverview, ILobbyOverviewController {

    public ListView lvLobbies;
    public TextField tfPrivateToken;
    public Button btnPrivateJoin;
    public Button btnCreateGame;
    private boolean showAlerts;

    private Timer timer;
    private LobbyShell selectedLobby;

    public void setShowAlerts(boolean showAlerts){
        this.showAlerts = showAlerts;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setListViewClickListener();
        refreshLobbies();
        startTimer();
    }

    @Override
    public void searchLobby(String lobbyName) {
        
    }

    @Override
    public void joinPublicLobby(LobbyShell lobby) {
        Connection.getInstance().joinLobby(lobby);
    }

    @Override
    public void joinPrivateLobby(LobbyShell lobby) {
        lobby.setToken(tfPrivateToken.getText());
        Connection.getInstance().joinLobby(lobby);
    }

    @Override
    public void createGame() {

    }

    public void btnCreateGamePressed(ActionEvent actionEvent) {

        timer.purge();
        timer.cancel();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage lobbyOverviewStage = (Stage)btnCreateGame.getScene().getWindow();
                ScreenLoader.getInstance().loadScreen(Screen.LOBBYCREATE, lobbyOverviewStage);
                lobbyOverviewStage.close();
            }
        });

    }

    @Override
    public void updateLobbyOverview(LobbyShell[] lobbies) {
        Platform.runLater(() -> {
            lvLobbies.getItems().clear();
            lvLobbies.getItems().addAll(lobbies);
            if(selectedLobby != null) {
                for(Object o : lvLobbies.getItems()) {
                    LobbyShell shell = (LobbyShell)o;
                    if(shell.getName().equals(selectedLobby.getName())) {
                        lvLobbies.getSelectionModel().select(o);
                    }
                }
            }
        });
    }

    @Override
    public void closeLobbyOverview(boolean succes, Lobby lobby) {
        if(succes) {

            timer.purge();
            timer.cancel();

            Platform.runLater(() -> {
                Stage lobbyCreateStage = (Stage)btnCreateGame.getScene().getWindow();
                ScreenLoader.getInstance().loadScreen(Screen.LOBBY, lobby, lobbyCreateStage);
                lobbyCreateStage.close();
            });

        }
    }

    @Override
    public void showLobbyJoinError(String errorMessage) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Something is wrong");
            alert.setContentText(errorMessage);

            alert.showAndWait();
        });
    }


    private void setListViewClickListener() {
        lvLobbies.setOnMouseClicked(click -> {
            if(click.getClickCount() == 2) {
                LobbyShell lobbyShell = (LobbyShell)lvLobbies.getSelectionModel().getSelectedItem();

                if(tfPrivateToken.getText().length() > 0)
                    joinPrivateLobby(lobbyShell);
                else
                    joinPublicLobby(lobbyShell);
            }
        });
    }

    public void btnJoinPressed(ActionEvent actionEvent) {
        if(lvLobbies.getSelectionModel().getSelectedItem() != null){
            LobbyShell lobbyShell = (LobbyShell)lvLobbies.getSelectionModel().getSelectedItem();

            if(tfPrivateToken.getText().length() > 0)
                joinPrivateLobby(lobbyShell);
            else
                joinPublicLobby(lobbyShell);
        }
    }

    private void refreshLobbies() {
        selectedLobby = (LobbyShell)lvLobbies.getSelectionModel().getSelectedItem();
        Platform.runLater(() -> lvLobbies.getItems().clear());
        Connection.getInstance().setLobbyOverviewController(this);
        try {
            Connection.getInstance().sendMessage(new WebSocketMessage(Header.REQUESTLOBBIES, null));
        } catch (NullPointerException e) {
            //Application was probably closed
            timer.purge();
            timer.cancel();
        }
    }


    private void startTimer() {
        TimerTask refreshTask = new TimerTask() {
            @Override
            public void run() {
                refreshLobbies();
            }
        };
        timer = new Timer();
        timer.schedule(refreshTask, 5000, 5000);
    }

    public void btnRefreshClick(ActionEvent actionEvent) {
        refreshLobbies();
    }
}
