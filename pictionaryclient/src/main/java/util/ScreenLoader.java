package util;

import controllers.EndGameController;
import controllers.LobbyController;
import controllers.PlayerCanvasController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Game;
import models.LeaderboardEntry;
import models.Lobby;

import java.util.List;

public class ScreenLoader {

    private static ScreenLoader instance;

    private ScreenLoader() {
    }

    public static ScreenLoader getInstance() {
        if (instance == null) {
            instance = new ScreenLoader();
        }
        return instance;
    }

    //This method should only be used when starting the client, for all other uses use one of the other 2 options
    public void loadScreen(Screen screen) {
        load(screen, null, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public void loadScreen(Screen screen, Object object, Stage origin) {
        load(screen, object, origin.getX(), origin.getY());
    }

    public void loadScreen(Screen screen, Stage origin) {
        load(screen, null, origin.getX(), origin.getY());
    }

    private void load(Screen screen, Object object, double x, double y) {
        String[] data = getData(screen);

        String fxmlFile = data[0];
        String title = data[1];

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = (Parent) fxmlLoader.load();
            if (object != null) {
                if (object instanceof Lobby) {
                    LobbyController lobbyController = fxmlLoader.<LobbyController>getController();
                    lobbyController.setLobby((Lobby) object);
                }
                if (object instanceof Game) {
                    PlayerCanvasController drawerCanvasController = fxmlLoader.<PlayerCanvasController>getController();
                    drawerCanvasController.setGame((Game)object);
                }
                if (object instanceof List) {
                    EndGameController endGameController = fxmlLoader.getController();
                    endGameController.setEndGame((List<LeaderboardEntry>)object);
                }
            }

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image(getClass().getResource("/icon.png").toExternalForm()));

            //This is only for when this gets called from the startup of the client, because there are no previous x/y coordinates
            if(x != Integer.MAX_VALUE || screen == Screen.GAME) {
                stage.setX(x);
                stage.setY(y);
            }

            if(screen == Screen.ENDGAME) {
                stage.initStyle(StageStyle.UNDECORATED);
            }

            stage.show();
        } catch (Exception e) {
            Alert screenLoadingAlert = new Alert(Alert.AlertType.ERROR, e.toString(), ButtonType.OK);
            screenLoadingAlert.show();
        }
    }

    private String[] getData(Screen screen) {
        String[] fxmlAndTitle = new String[2];
        switch (screen) {
            case LOBBY:
                fxmlAndTitle[0] = "/lobby.fxml";
                fxmlAndTitle[1] = "PictionaryIO | Lobby";
                break;
            case LOBBYOVERVIEW:
                fxmlAndTitle[0] = "/lobbyOverview.fxml";
                fxmlAndTitle[1] = "PictionaryIO | Lobby Overview";
                break;
            case LOBBYCREATE:
                fxmlAndTitle[0] = "/lobbyCreate.fxml";
                fxmlAndTitle[1] = "PictionaryIO | Create new lobby";
                break;
            case LOGIN:
                fxmlAndTitle[0] = "/login.fxml";
                fxmlAndTitle[1] = "PictionaryIO | Sign in";
                break;
            case REGISTER:
                fxmlAndTitle[0] = "/register.fxml";
                fxmlAndTitle[1] = "PictionaryIO | Register";
                break;
            case GAME:
                fxmlAndTitle[0] = "/DrawingViewPlayer.fxml";
                fxmlAndTitle[1] = "PictionaryIO | Active game";
                break;
            case ENDGAME:
                fxmlAndTitle[0] = "/endGame.fxml";
                fxmlAndTitle[1] = "PictionaryIO | Game results";
                break;
            default:
                fxmlAndTitle[0] = "";
                fxmlAndTitle[1] = "Pictionary.io";
                break;
        }
        return fxmlAndTitle;
    }

    public void displayAlert(String title, String message, Alert.AlertType type){
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(title);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
