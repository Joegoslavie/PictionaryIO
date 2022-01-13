package interfacesControllers;

import models.Lobby;
import models.LobbyShell;

public interface ILobbyOverviewController {
    void updateLobbyOverview(LobbyShell[] lobbies);
    void closeLobbyOverview(boolean succes, Lobby lobby);
    void showLobbyJoinError(String errorMessage);
}
