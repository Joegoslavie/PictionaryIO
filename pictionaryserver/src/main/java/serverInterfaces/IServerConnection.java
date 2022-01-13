package serverInterfaces;

import models.Game;
import models.Lobby;
import models.LobbyShell;
import models.Player;
import webSocketMessages.AccountDataEvent;
import webSocketMessages.PickWordEvent;
import webSocketMessages.WebSocketMessage;

import javax.websocket.Session;
import java.util.List;

public interface IServerConnection {
    void onMessage(String message, Session session);

    /*
    void handleLeaveGame();
    void handleChatMessage(WebSocketMessage msg);
    void endGame(Game game);
    void stopLobbyCreateTimer();

    void handlePickWordMessage(PickWordEvent pickWordEvent, Session session);
    void handleRegisterMessage(AccountDataEvent accountDataEventRegister, Session session);
    void handleLoginMessage(AccountDataEvent accountDataEventLogin, Session session);
    void handleLogoutMessage(AccountDataEvent accountDataEventLogout, Session session);
    void handleJoinLobbyMessage(LobbyShell lobbyShel1, Session session);
    void handleLeaveLobbyMessage(Session session);
    void handleGameStartMessage();

    Player login(String username, String password);
    Player register(String username, String password);
    void logout();
    void updateLeaderboardEntry(Game game);
    void startNewRound(Game game);
    void sendRandomWordsToPick(Session session);
    void createNewLobby(LobbyShell lobbyShell, List<Player> players);
    Lobby handleCreateLobbyMessage(LobbyShell shell, Player player);
    Lobby getLobbyFromId(int id);
    void sendMessageToPlayers(List<Player> players, WebSocketMessage msg);
    void update();*/
}
