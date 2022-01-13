package mocks;

import interfacesControllers.ILobbyController;
import models.Game;
import models.Player;

public class LobbyControllerMock implements ILobbyController {
    @Override
    public void updatePlayerStatus(Player player, Boolean joined) {

    }

    @Override
    public void updateGameStarted(Game game) {

    }
}
