package api.interfaces;

import restmodels.RESTPlayer;

public interface IPlayerRepository {
    RESTPlayer getPlayerByUsername(String username);
    RESTPlayer signInPlayer(RESTPlayer player);
    RESTPlayer registerPlayer(RESTPlayer player);

}
