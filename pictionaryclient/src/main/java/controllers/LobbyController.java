package controllers;

import enums.Header;
import interfacesControllers.ILobbyController;
import interfacesUI.ILobby;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logicModels.Connection;
import models.Game;
import models.Lobby;
import models.Player;
import util.Screen;
import util.ScreenLoader;
import webSocketMessages.WebSocketMessage;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController implements Initializable, ILobby, ILobbyController {

    public Label lblLobbyName;
    public Label lblRoomTokenTextHolder;
    public ListView lvPlayers;
    public Label lblPlayersJoined;
    public Button btnStartGame;
    public Button btnLeaveLobby;
    public TextField tfRoomToken;

    private boolean showAlerts;
    private Lobby lobby;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection.getInstance().setLobbyController(this);
    }

    @Override
    public void addPlayer(String playerName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removePlayer(int playerListPos) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removePlayer(String playerName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLobbyName(String newLobbyName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRoomToken(String newRoomToken) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void startGame() {
        String lobbyOwnerUsername = lobby.getPlayers().get(0).getUsername();
        String localUsername = Connection.getInstance().getLocalPlayer().getUsername();

        if (lobby != null && localUsername.equals(lobbyOwnerUsername)) {
            Connection.getInstance().startGame(this.lobby);
        }
    }

    public void setShowAlerts(boolean showAlerts) {
        this.showAlerts = showAlerts;
    }
    
    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
        updateLobbyInfo();
    }

    private void updateLobbyInfo() {
        Platform.runLater(() -> {
            lblLobbyName.setText(lobby.getName());
            tfRoomToken.setText(lobby.getToken());

            lvPlayers.getItems().clear();
            lvPlayers.getItems().addAll(lobby.getPlayers());
            tfRoomToken.setText(lobby.getToken());
            lblPlayersJoined.setText(lobby.getPlayers().size() + " of " + lobby.getNumPlayers() + " joined this lobby.");

                //Disable the start game button for players who are not the lobby owners
                Player lobbyOwner = (Player)lvPlayers.getItems().get(0);
                if(!Connection.getInstance().getLocalPlayer().getUsername().equals(lobbyOwner.getUsername())) {
                    btnStartGame.setVisible(false);
                } else {
                    btnStartGame.setVisible(true);
                }

            // if this matches we can start the game.
            if(lobby.getPlayers().size() >= 2){
                btnStartGame.setDisable(false);
            }
        });
    }

    // joined == false  -> player left
    // joined == true   -> player joined
    @Override
    public void updatePlayerStatus(Player player, Boolean joined) {
        if(joined)
            this.lobby.addPlayer(player);
        else
            this.lobby.removePlayer(player);

        updateLobbyInfo();
    }

    public void btnLeaveLobbyClick(ActionEvent actionEvent) {
        Connection.getInstance().sendMessage(new WebSocketMessage(Header.LOBBYLEAVE, null));
        returnToOverview();
    }

    private void returnToOverview() {
        Platform.runLater(() -> {
            Stage lobbyStage = (Stage) btnLeaveLobby.getScene().getWindow();
            ScreenLoader.getInstance().loadScreen(Screen.LOBBYOVERVIEW, lobbyStage);
            lobbyStage.close();
        });
    }

    @Override
    public void updateGameStarted(Game game) {
        Platform.runLater(() -> {
            Stage lobbyStage = (Stage)btnStartGame.getScene().getWindow();
            ScreenLoader.getInstance().loadScreen(Screen.GAME, game, lobbyStage);
            lobbyStage.close();
        });
    }

    public void StartGameButtonClick(ActionEvent actionEvent) {
        startGame();
    }


}
