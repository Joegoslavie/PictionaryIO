package controllers;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import models.LeaderboardEntry;
import util.ScoreComparator;

import java.net.URL;
import java.util.*;

public class EndGameController implements Initializable {
    public ListView lvLeaderboard;
    public Label lblNewLobbyTimer;

    private Timer timer;
    private int timerTicks = 10;

    public void setEndGame(List<LeaderboardEntry> leaderboardEntries) {
        Collections.sort(leaderboardEntries, new ScoreComparator());
        for (LeaderboardEntry leaderboardEntry : leaderboardEntries) {
            lvLeaderboard.getItems().add(leaderboardEntry.toString());
        }
    }

    public void startTimer() {
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(timerTicks > 0) {
                    updateLabel(timerTicks);
                } else if(timerTicks == 0 ) {
                    hideScreen();
                }
                else if(timerTicks == -2 ){
                    stopTimer();
                }

                timerTicks--;
            }
        }, 0, 1000);

    }

    private void hideScreen() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage endGameStage = (Stage) lvLeaderboard.getScene().getWindow();
                endGameStage.hide();
            }
        });
    }

    private void stopTimer() {
        timer.purge();
        timer.cancel();

        Platform.runLater(() -> {
            Stage endGameStage = (Stage) lblNewLobbyTimer.getScene().getWindow();
            endGameStage.close();
        });
    }

    private void updateLabel(int tick) {
        Platform.runLater(() -> lblNewLobbyTimer.setText("Time to new lobby: " + tick));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startTimer();
    }
}
