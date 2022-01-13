package main;

import com.google.gson.Gson;
import enums.Header;
import enums.MessageType;
import mocks.MockRemoteEndPoint;
import mocks.MockRestClient;
import mocks.MockSession;
import models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import webSocketMessages.AccountDataEvent;
import webSocketMessages.PickWordEvent;
import webSocketMessages.WebSocketMessage;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectionTest {

    Connection connection;
    MockSession mockSession;
    MockSession mockSession1;
    MockRestClient mockRestClient;
    MockRemoteEndPoint mockRemoteEndPoint;
    Gson gson;
    boolean exceptionThrown;

    @BeforeEach
    public void setUp(){
        connection = new Connection();
        mockRestClient = new MockRestClient();
        mockRemoteEndPoint = new MockRemoteEndPoint();
        mockSession = new MockSession(mockRemoteEndPoint);
        mockSession1 = new MockSession(mockRemoteEndPoint);
        gson = new Gson();
        exceptionThrown = false;
        connection.onOpen(mockSession);
    }

    @Test
    public void chatMessageIncorrectTest(){
        connection.setLocalPlayer(new Player("username"));
        LobbyShell lobbyShell = new LobbyShell(2, "LobbyShell", false, 2);
        Lobby lobby = new Lobby(lobbyShell);
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        lobby.addPlayer(player1);
        lobby.addPlayer(player2);
        player1.setSession(mockSession);
        player2.setSession(mockSession);
        connection.putEntryInGameMap(mockSession, new Game("Game", lobby));
        Message message = new Message(new Date(), "Hoi", "username", MessageType.DEFAULT);
        String messageJson = gson.toJson(message);
        WebSocketMessage webSocketMessage = new WebSocketMessage(Header.CHATMESSAGE, messageJson);
        String webSocketMessageJson = gson.toJson(webSocketMessage);
        try{
            connection.onMessage(webSocketMessageJson, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertTrue(mockRemoteEndPoint.getSent());
    }

    @Test
    public void chatMessageCorrectTest(){
        connection.setLocalPlayer(new Player("player1"));
        LobbyShell lobbyShell = new LobbyShell(2, "LobbyShell", false, 2);
        Lobby lobby = new Lobby(lobbyShell);
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        lobby.addPlayer(player1);
        lobby.addPlayer(player2);
        player1.setSession(mockSession);
        player2.setSession(mockSession);
        Game game = new Game("Game", lobby);
        game.getCurrentRound().setWordToDraw("Partijkartel");
        connection.putEntryInGameMap(mockSession, game);
        Message message = new Message(new Date(), "Partijkartel", "username", MessageType.DEFAULT);
        String messageJson = gson.toJson(message);
        WebSocketMessage webSocketMessage = new WebSocketMessage(Header.CHATMESSAGE, messageJson);
        String webSocketMessageJson = gson.toJson(webSocketMessage);
        try{
            connection.onMessage(webSocketMessageJson, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertTrue(mockRemoteEndPoint.getSent());
    }

    @Test
    public void createLobbyTest(){
        Player player = new Player("player1");
        player.setSession(mockSession);
        connection.setLocalPlayer(player);

        LobbyShell lobbyShell = new LobbyShell(2, "LobbyShell", false, 1);
        String lobbyShellJson = gson.toJson(lobbyShell);
        WebSocketMessage webSocketMessage = new WebSocketMessage(Header.LOBBYCREATE, lobbyShellJson);
        String webSocketMessageJson = gson.toJson(webSocketMessage);
        try{
            connection.onMessage(webSocketMessageJson, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertTrue(mockRemoteEndPoint.getSent());
    }

    @Test
    public void requestLobbiesTest(){
        //create lobby
        Player player = new Player("player1");
        player.setSession(mockSession);
        connection.setLocalPlayer(player);

        LobbyShell lobbyShell = new LobbyShell(2, "LobbyShell", false, 1);
        String lobbyShellJson = gson.toJson(lobbyShell);
        WebSocketMessage webSocketMessage = new WebSocketMessage(Header.LOBBYCREATE, lobbyShellJson);
        String webSocketMessageJson = gson.toJson(webSocketMessage);

        connection.onMessage(webSocketMessageJson, mockSession);

        //request lobbies
        WebSocketMessage webSocketMessage1 = new WebSocketMessage(Header.REQUESTLOBBIES, null);
        String webSocketMessageJson1 = gson.toJson(webSocketMessage1);
        try{
            connection.onMessage(webSocketMessageJson1, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertTrue(mockRemoteEndPoint.getSent());
    }

    @Test
    public void leaveLobbyTest(){
        //create lobby
        Player player = new Player("player1");
        player.setSession(mockSession);
        connection.setLocalPlayer(player);

        LobbyShell lobbyShell = new LobbyShell(2, "LobbyShell", false, 1);
        String lobbyShellJson = gson.toJson(lobbyShell);
        WebSocketMessage webSocketMessage = new WebSocketMessage(Header.LOBBYCREATE, lobbyShellJson);
        String webSocketMessageJson = gson.toJson(webSocketMessage);

        connection.onMessage(webSocketMessageJson, mockSession);

        //leave lobby
        WebSocketMessage webSocketMessage1 = new WebSocketMessage(Header.LOBBYLEAVE, null);
        String webSocketMessageJson1 = gson.toJson(webSocketMessage1);
        try{
            connection.onMessage(webSocketMessageJson1, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertTrue(mockRemoteEndPoint.getSent());
    }

    @Test
    public void joinLobbytest(){
        //create lobby
        Player player = new Player("player1");
        player.setSession(mockSession);
        connection.setLocalPlayer(player);

        LobbyShell lobbyShell = new LobbyShell(2, "LobbyShell", false, 1);
        String lobbyShellJson = gson.toJson(lobbyShell);
        WebSocketMessage webSocketMessage = new WebSocketMessage(Header.LOBBYCREATE, lobbyShellJson);
        String webSocketMessageJson = gson.toJson(webSocketMessage);

        connection.onMessage(webSocketMessageJson, mockSession);

        //join lobby
        WebSocketMessage webSocketMessage1 = new WebSocketMessage(Header.LOBBYJOIN, lobbyShellJson);
        String webSocketMessageJson1 = gson.toJson(webSocketMessage1);
        try{
            connection.onMessage(webSocketMessageJson1, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertTrue(mockRemoteEndPoint.getSent());
    }

    @Test
    public void searchLobbyTest(){
        //create lobby
        Player player = new Player("player1");
        player.setSession(mockSession);
        connection.setLocalPlayer(player);

        LobbyShell lobbyShell = new LobbyShell(2, "LobbyShell", false, 1);
        String lobbyShellJson = gson.toJson(lobbyShell);
        WebSocketMessage webSocketMessage = new WebSocketMessage(Header.LOBBYCREATE, lobbyShellJson);
        String webSocketMessageJson = gson.toJson(webSocketMessage);

        connection.onMessage(webSocketMessageJson, mockSession);

        //search lobby
        WebSocketMessage webSocketMessage1 = new WebSocketMessage(Header.SEARCHLOBBY, "LobbyShell");
        String webSocketMessageJson1 = gson.toJson(webSocketMessage1);
        try{
            connection.onMessage(webSocketMessageJson1, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        //not implemented, so exception must be thrown
        assertTrue(exceptionThrown);
        assertTrue(mockRemoteEndPoint.getSent());
    }

    @Test
    public void startGameTest(){
        //create lobby
        Player player = new Player("player1");
        player.setSession(mockSession);
        connection.setLocalPlayer(player);

        LobbyShell lobbyShell = new LobbyShell(2, "LobbyShell", false, 1);
        String lobbyShellJson = gson.toJson(lobbyShell);
        WebSocketMessage webSocketMessage = new WebSocketMessage(Header.LOBBYCREATE, lobbyShellJson);
        String webSocketMessageJson = gson.toJson(webSocketMessage);

        connection.onMessage(webSocketMessageJson, mockSession);

        //start game
        WebSocketMessage webSocketMessage1 = new WebSocketMessage(Header.GAMESTART, null);
        String webSocketMessageJson1 = gson.toJson(webSocketMessage1);
        try{
            connection.onMessage(webSocketMessageJson1, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertTrue(mockRemoteEndPoint.getSent());
    }

    @Test
    public void leaveGameTest(){
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        player1.setSession(mockSession);
        player2.setSession(mockSession1);
        connection.setLocalPlayer(player1);

        //lobby create
        LobbyShell lobbyShell = new LobbyShell(2, "LobbyShell", false, 2);
        lobbyShell.setId(2);
        String lobbyShellJson = gson.toJson(lobbyShell);
        WebSocketMessage webSocketMessage = new WebSocketMessage(Header.LOBBYCREATE, lobbyShellJson);
        String webSocketMessageJson = gson.toJson(webSocketMessage);

        connection.onMessage(webSocketMessageJson, mockSession);


        //join lobby
        WebSocketMessage webSocketMessage1 = new WebSocketMessage(Header.LOBBYJOIN, lobbyShellJson);
        String webSocketMessageJson1 = gson.toJson(webSocketMessage1);

        connection.onMessage(webSocketMessageJson1, mockSession);
        connection.onMessage(webSocketMessageJson1, mockSession1);

        //start game
        WebSocketMessage webSocketMessage2 = new WebSocketMessage(Header.GAMESTART, null);
        String webSocketMessageJson2 = gson.toJson(webSocketMessage2);

        connection.onMessage(webSocketMessageJson2, mockSession);

        //leave game
        WebSocketMessage webSocketMessage3 = new WebSocketMessage(Header.GAMELEAVE, null);
        String webSocketMessageJson3 = gson.toJson(webSocketMessage3);
        try{
            connection.onMessage(webSocketMessageJson3, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertTrue(mockRemoteEndPoint.getSent());
    }

    @Test
    public void pickWordTest(){
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        player1.setSession(mockSession);
        player2.setSession(mockSession1);
        connection.setLocalPlayer(player1);

        //lobby create
        LobbyShell lobbyShell = new LobbyShell(2, "LobbyShell", false, 2);
        lobbyShell.setId(3);
        String lobbyShellJson = gson.toJson(lobbyShell);
        WebSocketMessage webSocketMessage = new WebSocketMessage(Header.LOBBYCREATE, lobbyShellJson);
        String webSocketMessageJson = gson.toJson(webSocketMessage);

        connection.onMessage(webSocketMessageJson, mockSession);


        //join lobby
        WebSocketMessage webSocketMessage1 = new WebSocketMessage(Header.LOBBYJOIN, lobbyShellJson);
        String webSocketMessageJson1 = gson.toJson(webSocketMessage1);

        connection.onMessage(webSocketMessageJson1, mockSession);
        connection.onMessage(webSocketMessageJson1, mockSession1);

        //start game
        WebSocketMessage webSocketMessage2 = new WebSocketMessage(Header.GAMESTART, null);
        String webSocketMessageJson2 = gson.toJson(webSocketMessage2);

        connection.onMessage(webSocketMessageJson2, mockSession);

        //pick word
        ArrayList<String> words = new ArrayList<>();
        words.add("lol1");
        words.add("lol2");
        words.add("lol3");
        PickWordEvent pickWordEvent = new PickWordEvent(words, "lol1");
        String pickWordEventJson = gson.toJson(pickWordEvent);
        WebSocketMessage webSocketMessage3 = new WebSocketMessage(Header.PICKWORD, pickWordEventJson);
        String webSocketMessageJson3 = gson.toJson(webSocketMessage3);
        try{
            connection.onMessage(webSocketMessageJson3, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertTrue(mockRemoteEndPoint.getSent());
    }

    @Test
    public void pointTest(){
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        player1.setSession(mockSession);
        player2.setSession(mockSession1);
        connection.setLocalPlayer(player1);

        //lobby create
        LobbyShell lobbyShell = new LobbyShell(2, "LobbyShell", false, 2);
        lobbyShell.setId(5);
        String lobbyShellJson = gson.toJson(lobbyShell);
        WebSocketMessage webSocketMessage = new WebSocketMessage(Header.LOBBYCREATE, lobbyShellJson);
        String webSocketMessageJson = gson.toJson(webSocketMessage);

        connection.onMessage(webSocketMessageJson, mockSession);


        //join lobby
        WebSocketMessage webSocketMessage1 = new WebSocketMessage(Header.LOBBYJOIN, lobbyShellJson);
        String webSocketMessageJson1 = gson.toJson(webSocketMessage1);

        connection.onMessage(webSocketMessageJson1, mockSession);
        connection.onMessage(webSocketMessageJson1, mockSession1);

        //start game
        WebSocketMessage webSocketMessage2 = new WebSocketMessage(Header.GAMESTART, null);
        String webSocketMessageJson2 = gson.toJson(webSocketMessage2);

        connection.onMessage(webSocketMessageJson2, mockSession);

        //pick word
        ArrayList<String> words = new ArrayList<>();
        words.add("lol1");
        words.add("lol2");
        words.add("lol3");
        PickWordEvent pickWordEvent = new PickWordEvent(words, "lol1");
        String pickWordEventJson = gson.toJson(pickWordEvent);
        WebSocketMessage webSocketMessage3 = new WebSocketMessage(Header.PICKWORD, pickWordEventJson);
        String webSocketMessageJson3 = gson.toJson(webSocketMessage3);

        connection.onMessage(webSocketMessageJson3, mockSession);

        //point
        WebSocketMessage webSocketMessage4 = new WebSocketMessage(Header.POINT, null);
        String webSocketMessageJson4 = gson.toJson(webSocketMessage4);
        try{
            connection.onMessage(webSocketMessageJson4, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertTrue(mockRemoteEndPoint.getSent());
    }

    @Test
    public void clearCanvasTest(){
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        player1.setSession(mockSession);
        player2.setSession(mockSession1);
        connection.setLocalPlayer(player1);

        //lobby create
        LobbyShell lobbyShell = new LobbyShell(2, "LobbyShell", false, 2);
        lobbyShell.setId(4);
        String lobbyShellJson = gson.toJson(lobbyShell);
        WebSocketMessage webSocketMessage = new WebSocketMessage(Header.LOBBYCREATE, lobbyShellJson);
        String webSocketMessageJson = gson.toJson(webSocketMessage);

        connection.onMessage(webSocketMessageJson, mockSession);


        //join lobby
        WebSocketMessage webSocketMessage1 = new WebSocketMessage(Header.LOBBYJOIN, lobbyShellJson);
        String webSocketMessageJson1 = gson.toJson(webSocketMessage1);

        connection.onMessage(webSocketMessageJson1, mockSession);
        connection.onMessage(webSocketMessageJson1, mockSession1);

        //start game
        WebSocketMessage webSocketMessage2 = new WebSocketMessage(Header.GAMESTART, null);
        String webSocketMessageJson2 = gson.toJson(webSocketMessage2);

        connection.onMessage(webSocketMessageJson2, mockSession);

        //clear canvas
        WebSocketMessage webSocketMessage3 = new WebSocketMessage(Header.CLEARCANVAS, null);
        String webSocketMessageJson3 = gson.toJson(webSocketMessage3);
        try{
            connection.onMessage(webSocketMessageJson3, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertTrue(mockRemoteEndPoint.getSent());
    }
}