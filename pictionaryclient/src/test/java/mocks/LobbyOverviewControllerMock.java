package mocks;

import controllers.LobbyOverviewController;
import interfacesControllers.ILobbyOverviewController;
import models.Lobby;
import models.LobbyShell;

public class LobbyOverviewControllerMock implements ILobbyOverviewController {
    @Override
    public void updateLobbyOverview(LobbyShell[] lobbies) {

    }

    @Override
    public void closeLobbyOverview(boolean succes, Lobby lobby) {

    }

    @Override
    public void showLobbyJoinError(String errorMessage) {

    }
}
