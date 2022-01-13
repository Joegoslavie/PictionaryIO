package interfacesUI;

import models.LobbyShell;

public interface ILobbyOverview {

    void searchLobby(String lobbyName);
    void joinPublicLobby(LobbyShell lobby);
    void joinPrivateLobby(LobbyShell lobby);
    void createGame();
}
