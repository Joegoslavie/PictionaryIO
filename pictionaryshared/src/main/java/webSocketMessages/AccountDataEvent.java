package webSocketMessages;

import models.Player;

public class AccountDataEvent {
    public Player player;
    public String username;
    public String password;

    public AccountDataEvent(){}
    public AccountDataEvent(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
