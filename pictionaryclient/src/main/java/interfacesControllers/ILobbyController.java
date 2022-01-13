package interfacesControllers;

import models.Game;
import models.Player;

public interface ILobbyController {
    void updatePlayerStatus(Player player, Boolean joined);
    void updateGameStarted(Game game);
}
