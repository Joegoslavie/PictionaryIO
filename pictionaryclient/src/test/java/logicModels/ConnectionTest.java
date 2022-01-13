package logicModels;

import com.google.gson.Gson;
import controllers.RegisterController;
import enums.Header;
import enums.MessageType;
import interfacesControllers.IRegisterController;
import mocks.*;
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
    Gson gson;
    MockSession mockSession;
    MockRemoteEndPoint mockRemoteEndPoint;
    RegisterControllerMock registerControllerMock;
    LoginControllerMock loginControllerMock;
    PlayerCanvasControllerMock playerCanvasControllerMock;
    LobbyControllerMock lobbyControllerMock;
    LobbyCreateControllerMock lobbyCreateControllerMock;
    LobbyOverviewControllerMock lobbyOverviewControllerMock;
    boolean exceptionThrown;

    @BeforeEach
    public void setUp(){
        connection = Connection.getInstance();
        gson = new Gson();
        registerControllerMock = new RegisterControllerMock();
        loginControllerMock = new LoginControllerMock();
        playerCanvasControllerMock = new PlayerCanvasControllerMock();
        lobbyControllerMock = new LobbyControllerMock();
        lobbyCreateControllerMock = new LobbyCreateControllerMock();
        lobbyOverviewControllerMock = new LobbyOverviewControllerMock();
        connection.setRegisterController(registerControllerMock);
        connection.setLogin(loginControllerMock);
        connection.setDrawerController(playerCanvasControllerMock);
        connection.setLobbyController(lobbyControllerMock);
        connection.setLobbyCreateController(lobbyCreateControllerMock);
        connection.setLobbyOverviewController(lobbyOverviewControllerMock);
        mockRemoteEndPoint = new MockRemoteEndPoint();
        mockSession = new MockSession(mockRemoteEndPoint);
        exceptionThrown = false;
        connection.onOpen(mockSession);
    }

    @Test
    public void registerTest(){
        Player player = new Player("username");
        String playerJson = gson.toJson(player);
        WebSocketMessage webSocketMessage = new WebSocketMessage(Header.REGISTER, playerJson);
        String webSocketMessageJson = gson.toJson(webSocketMessage);
        try{
            connection.onMessage(webSocketMessageJson, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void loginTest(){
        AccountDataEvent accountDataEvent = new AccountDataEvent("username", "password");
        String accountDataEventJson = gson.toJson(accountDataEvent);
        WebSocketMessage webSocketMessage = new WebSocketMessage(Header.LOGIN, accountDataEventJson);
        String webSocketMessageJson = gson.toJson(webSocketMessage);
        try{
            connection.onMessage(webSocketMessageJson, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void chatMessageTest(){
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
    }

    @Test
    public void pointTest(){
        LinePoints linePoints = new LinePoints(1, 1, 0, 0, "Red", 1.00);
        String linePointsJson = gson.toJson(linePoints);
        WebSocketMessage webSocketMessage = new WebSocketMessage(Header.POINT, linePointsJson);
        String webSocketMessageJson = gson.toJson(webSocketMessage);
        try{
            connection.onMessage(webSocketMessageJson, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void canvasClearTest(){
        WebSocketMessage webSocketMessage = new WebSocketMessage(Header.CLEARCANVAS, null);
        String webSocketMessageJson = gson.toJson(webSocketMessage);
        try{
            connection.onMessage(webSocketMessageJson, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void lobbyCreateTest(){
        LobbyShell lobbyShell = new LobbyShell(2, "lobbyshell", false, 1);
        Lobby lobby = new Lobby(lobbyShell);
        String lobbyJson = gson.toJson(lobby);
        WebSocketMessage webSocketMessage = new WebSocketMessage(Header.LOBBYCREATE, lobbyJson);
        String webSocketMessageJson = gson.toJson(webSocketMessage);
        try{
            connection.onMessage(webSocketMessageJson, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void requestLobbiesTest(){
        LobbyShell[] lobbyShells = new LobbyShell[2];
        LobbyShell lobbyShell1 = new LobbyShell(2, "lobbyshell1", false, 1);
        LobbyShell lobbyShell2 = new LobbyShell(2, "lobbyshell2", false, 1);
        lobbyShells[0] = lobbyShell1;
        lobbyShells[1] = lobbyShell2;
        String lobbyShellsJson = gson.toJson(lobbyShells);
        WebSocketMessage webSocketMessage = new WebSocketMessage(Header.REQUESTLOBBIES, lobbyShellsJson);
        String webSocketMessageJson = gson.toJson(webSocketMessage);
        try{
            connection.onMessage(webSocketMessageJson, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void lobbyJoinTest(){
        LobbyShell lobbyShell = new LobbyShell(2, "lobbyshell1", false, 1);
        Lobby lobby = new Lobby(lobbyShell);
        String lobbyJson = gson.toJson(lobby);
        WebSocketMessage webSocketMessage = new WebSocketMessage(Header.LOBBYJOIN, lobbyJson);
        String webSocketMessageJson = gson.toJson(webSocketMessage);
        try{
            connection.onMessage(webSocketMessageJson, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void lobbyLeaveTest(){
        LobbyShell lobbyShell = new LobbyShell(2, "lobbyshell1", false, 1);
        Lobby lobby = new Lobby(lobbyShell);
        String lobbyJson = gson.toJson(lobby);
        WebSocketMessage webSocketMessage = new WebSocketMessage(Header.LOBBYJOIN, lobbyJson);
        String webSocketMessageJson = gson.toJson(webSocketMessage);

        connection.onMessage(webSocketMessageJson, mockSession);

        Player player = new Player("username");
        String playerJson = gson.toJson(player);
        WebSocketMessage webSocketMessage1 = new WebSocketMessage(Header.LOBBYLEAVE, playerJson);
        String webSocketMessageJson1 = gson.toJson(webSocketMessage1);
        try{
            connection.onMessage(webSocketMessageJson1, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void newPlayerJoined(){
        Player player = new Player("username");
        String playerJson = gson.toJson(player);
        WebSocketMessage webSocketMessage1 = new WebSocketMessage(Header.NEWPLAYERJOINED, playerJson);
        String webSocketMessageJson1 = gson.toJson(webSocketMessage1);
        try{
            connection.onMessage(webSocketMessageJson1, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void lobbyJoinErrorTest(){
        Player player = new Player("username");
        String playerJson = gson.toJson(player);
        WebSocketMessage webSocketMessage1 = new WebSocketMessage(Header.LOBBYJOINERROR, playerJson);
        String webSocketMessageJson1 = gson.toJson(webSocketMessage1);
        try{
            connection.onMessage(webSocketMessageJson1, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void gameStartTest(){
        LobbyShell lobbyShell = new LobbyShell(2, "lobbyshell1", false, 1);
        Lobby lobby = new Lobby(lobbyShell);
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        lobby.addPlayer(player1);
        lobby.addPlayer(player2);
        connection.setLocalPlayer(player1);
        Game game = new Game("gamename", lobby);
        String gameJson = gson.toJson(game);
        WebSocketMessage webSocketMessage1 = new WebSocketMessage(Header.GAMESTART, gameJson);
        String webSocketMessageJson1 = gson.toJson(webSocketMessage1);
        try{
            connection.onMessage(webSocketMessageJson1, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void pickWordTest(){
        ArrayList<String> words = new ArrayList<>();
        words.add("lol1");
        words.add("lol2");
        words.add("lol3");
        PickWordEvent pickWordEvent = new PickWordEvent(words, "lol1");
        String pickWordEventJson = gson.toJson(pickWordEvent);
        WebSocketMessage webSocketMessage1 = new WebSocketMessage(Header.PICKWORD, pickWordEventJson);
        String webSocketMessageJson1 = gson.toJson(webSocketMessage1);
        try{
            connection.onMessage(webSocketMessageJson1, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void roundUpdateTest(){
        LobbyShell lobbyShell = new LobbyShell(2, "lobbyshell1", false, 1);
        Lobby lobby = new Lobby(lobbyShell);
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        lobby.addPlayer(player1);
        lobby.addPlayer(player2);
        connection.setLocalPlayer(player1);
        Game game = new Game("gamename", lobby);
        connection.setGame(game);

        Round round = new Round("username");
        String roundJson = gson.toJson(round);

        WebSocketMessage webSocketMessage1 = new WebSocketMessage(Header.ROUNDUPDATE, roundJson);
        String webSocketMessageJson1 = gson.toJson(webSocketMessage1);
        try{
            connection.onMessage(webSocketMessageJson1, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void wordChosenTest(){
        String word = "1";
        String wordJson = gson.toJson(word);

        WebSocketMessage webSocketMessage1 = new WebSocketMessage(Header.WORDCHOSEN, wordJson);
        String webSocketMessageJson1 = gson.toJson(webSocketMessage1);
        try{
            connection.onMessage(webSocketMessageJson1, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void leaderboardUpdateTest(){
        LeaderboardEntry leaderboardEntry = new LeaderboardEntry("username", 100);
        String leaderboardEntryJson = gson.toJson(leaderboardEntry);

        WebSocketMessage webSocketMessage1 = new WebSocketMessage(Header.LEADERBOARDUPDATE, leaderboardEntryJson);
        String webSocketMessageJson1 = gson.toJson(webSocketMessage1);
        try{
            connection.onMessage(webSocketMessageJson1, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void gameLeaveTest(){
        Player player = new Player("username");
        String playerJson = gson.toJson(player);

        WebSocketMessage webSocketMessage1 = new WebSocketMessage(Header.GAMELEAVE, playerJson);
        String webSocketMessageJson1 = gson.toJson(webSocketMessage1);
        try{
            connection.onMessage(webSocketMessageJson1, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void endGameTest(){
        WebSocketMessage webSocketMessage1 = new WebSocketMessage(Header.ENDGAME, null);
        String webSocketMessageJson1 = gson.toJson(webSocketMessage1);
        try{
            connection.onMessage(webSocketMessageJson1, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void roundWordTest(){
        String word = "lol1";
        String wordJson = gson.toJson(word);
        WebSocketMessage webSocketMessage1 = new WebSocketMessage(Header.ROUNDWORD, wordJson);
        String webSocketMessageJson1 = gson.toJson(webSocketMessage1);
        try{
            connection.onMessage(webSocketMessageJson1, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void wordAlmostCorrectTest(){
        String word = "lol1";
        String wordJson = gson.toJson(word);
        WebSocketMessage webSocketMessage1 = new WebSocketMessage(Header.WORDALMOSTCORRECT, wordJson);
        String webSocketMessageJson1 = gson.toJson(webSocketMessage1);
        try{
            connection.onMessage(webSocketMessageJson1, mockSession);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }
}