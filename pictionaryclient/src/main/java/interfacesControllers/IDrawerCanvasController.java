package interfacesControllers;

import models.LeaderboardEntry;
import models.Message;
import models.Player;
import models.Round;

import java.util.List;

public interface IDrawerCanvasController {
    void showChatMessage(Message message);
    void drawRemotePoint(double lastX, double lastY, double x, double y, String color, double brushSize);
    void clear();
    void showWords(List<String> words);
    void updateRound(Round round);
    void playerLeftEvent(Player player);
    void setCanDraw(boolean canDraw);
    void roundStart(int time);
    void guessedWord(LeaderboardEntry leaderboardEntry);
    void updateLeaderboard(LeaderboardEntry leaderboardEntry);
    void updateCurrentDrawerName(String drawerName);
    void updateEndGame();
    void showRoundWord(String roundWord);
    void showGuessAlmostCorrect(String guessedWord);
}