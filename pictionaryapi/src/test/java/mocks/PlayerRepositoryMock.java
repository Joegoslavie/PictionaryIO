package mocks;

import api.interfaces.IPlayerRepository;
import restmodels.RESTPlayer;

public class PlayerRepositoryMock implements IPlayerRepository {
    @Override
    public RESTPlayer getPlayerByUsername(String username) {
        return null;
    }

    @Override
    public RESTPlayer signInPlayer(RESTPlayer player) {
        return null;
    }

    @Override
    public RESTPlayer registerPlayer(RESTPlayer player) {
        return null;
    }
}
