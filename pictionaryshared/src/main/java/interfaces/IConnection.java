package interfaces;

import models.Player;
import webSocketMessages.WebSocketMessage;

import javax.websocket.Session;

public interface IConnection {
    /*void onOpen();
    void onClose();
    void onMessage();
    void onError();*/

    Player login(String username, String password);
    Player register(String username, String password);
    void sendMessage(WebSocketMessage message, Session session);
    void sendMessage(WebSocketMessage message);
    void logout();
}
