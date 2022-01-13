package mocks;

import interfacesControllers.IDrawerCanvasController;
import models.LeaderboardEntry;
import models.Message;
import models.Player;
import models.Round;

import java.util.List;

public class PlayerCanvasControllerMock implements IDrawerCanvasController {
    @Override
    public void showChatMessage(Message message) {

    }

    @Override
    public void drawRemotePoint(double lastX, double lastY, double x, double y, String color, double brushSize) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void showWords(List<String> words) {

    }

    @Override
    public void updateRound(Round round) {

    }

    @Override
    public void playerLeftEvent(Player player) {

    }

    @Override
    public void setCanDraw(boolean canDraw) {

    }

    @Override
    public void roundStart(int time) {

    }

    @Override
    public void guessedWord(LeaderboardEntry leaderboardEntry) {

    }

    @Override
    public void updateLeaderboard(LeaderboardEntry leaderboardEntry) {

    }

    @Override
    public void updateCurrentDrawerName(String drawerName) {

    }

    @Override
    public void updateEndGame() {

    }

    @Override
    public void showRoundWord(String roundWord) {

    }

    @Override
    public void showGuessAlmostCorrect(String guessedWord) {

    }
}
