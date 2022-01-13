package mocks;

import api.interfaces.IAccountController;

public class AccountControllerMock implements IAccountController {
    @Override
    public Object register(String username, String password) {
        return null;
    }

    @Override
    public Object login(String username, String password) {
        return null;
    }

    @Override
    public Object signOut(String username) {
        return null;
    }
}
