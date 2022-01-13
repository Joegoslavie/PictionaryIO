package logicModels;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import enums.Header;
import interfaces.IConnection;
import interfacesControllers.*;
import javafx.scene.control.Alert;
import models.*;
import org.eclipse.jetty.util.component.LifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ScreenLoader;
import webSocketMessages.AccountDataEvent;
import webSocketMessages.PickWordEvent;
import webSocketMessages.WebSocketMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

@ClientEndpoint
public class Connection implements IConnection {

    private final String uri = "ws://145.93.40.247:8095/communicator/";

    private Session session;
    private Gson gson = null;

    private static final Logger log = LoggerFactory.getLogger(Connection.class);

    private static Connection instance;
    private static WebSocketContainer container;

    private IDrawerCanvasController canvas;
    private IRegisterController register;
    private ILoginController login;
    private ILobbyCreateController lobbyCreateController;
    private ILobbyOverviewController lobbyOverviewController;
    private ILobbyController lobbyController;

    private Player localPlayer;
    private ArrayList<String> wordsToPick;
    private Game game;
    private Lobby lobby;

    private boolean isDrawer = false;

    public Player getLocalPlayer() {
        return this.localPlayer;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public void setLocalPlayer(Player player){
        this.localPlayer = player;
    }

    public void setGame(Game game){
        this.game = game;
    }

    private Connection() {
        log.info("Connection class instantiated");
        this.gson = new Gson();
        startClient();
    }

    public static Connection getInstance() {
        if (instance == null) {
            instance = new Connection();
        }
        return instance;
    }

    public Session getSession() {
        return this.session;
    }

    @OnOpen
    public void onOpen(Session session) {
        log.info("Websocket client session open: " + session.getRequestURI());
        this.session = session;
    }

    @OnClose
    public void onClose(CloseReason reason) {
        log.info("Websocket client close session: " + session.getRequestURI());
        log.info("Close reason: " + reason);
        session = null;
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("Websocket client message received: " + message);
        processMessage(message);
    }

    @OnError
    public void onError(Session session, Throwable cause) {
        log.error("Websocket client connection error: " + cause);
    }

    public void sendMessage(WebSocketMessage msg, Session session) {
        String jsonMsg = gson.toJson(msg);
        session.getAsyncRemote().sendText(jsonMsg);
    }

    @Override
    public void sendMessage(WebSocketMessage message) {
        sendMessage(message, this.session);
    }

    @Override
    public Player login(String username, String password) {
        AccountDataEvent accountDataEvent = new AccountDataEvent(username, password);
        String accountDataEventMsg = gson.toJson(accountDataEvent);

        WebSocketMessage msg = new WebSocketMessage(Header.LOGIN, accountDataEventMsg);
        sendMessage(msg, session);
        return null;
    }

    @Override
    public Player register(String username, String password) {
        AccountDataEvent accountDataEvent = new AccountDataEvent(username, password);
        String accountDataEventMsg = gson.toJson(accountDataEvent);

        WebSocketMessage msg = new WebSocketMessage(Header.REGISTER, accountDataEventMsg);
        sendMessage(msg, session);
        return null;
    }

    @Override
    public void logout() {
        WebSocketMessage msg = new WebSocketMessage(Header.LOGOUT, "");
        sendMessage(msg, session);
    }

    public void joinLobby(LobbyShell lobbyShell) {
        WebSocketMessage msg = new WebSocketMessage(Header.LOBBYJOIN, gson.toJson(lobbyShell));
        sendMessage(msg);
    }

    public void startGame(Lobby lobby) {
        WebSocketMessage msg = new WebSocketMessage(Header.GAMESTART, gson.toJson(lobby));
        sendMessage(msg);
    }

    public void sendPickWord(String word) {
        PickWordEvent pickWordEvent = new PickWordEvent(word);
        WebSocketMessage msg = new WebSocketMessage(Header.PICKWORD, gson.toJson(pickWordEvent));
        sendMessage(msg);
    }

    private void startClient() {
        log.info("Websocket client starting");

        try {
            container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(uri));
        } catch (IOException | URISyntaxException | DeploymentException exc) {
            log.error("Error occured: " + exc);
        }
    }

    public void stopClient() {
        log.info("Websocket client stopping");

        try {
            if (session != null) {
                if(game != null) {
                    sendMessage(new WebSocketMessage(Header.GAMELEAVE, null));
                } else if(lobby != null) {
                    sendMessage(new WebSocketMessage(Header.LOBBYLEAVE, null));
                }

                session.close();
            }

            if (container != null) {
                try {
                    ((LifeCycle) container).stop();
                } catch (Exception exc) {
                    log.error(exc.toString());
                }
            }
        } catch (IOException exc) {
            log.info(String.valueOf(exc));
        }
    }

    private void processMessage(String jsonMessage) {
        WebSocketMessage msg;

        try {
            msg = gson.fromJson(jsonMessage, WebSocketMessage.class);
        } catch (JsonSyntaxException exc) {
            log.error("Error occured: " + exc);
            return;
        }

        Header header = msg.getHeader();
        if (header == null) {
            log.info("Invalid WEbSocketMessageOperation Received");
            return;
        }

        // Duplicate variables used in the switch statement below
        boolean success;
        String drawerName;
        String myName;
        Player leavingPlayer;

        switch(header) {
            case REGISTER:
                handleRegister(msg);
                break;
            case LOGIN:
                handleLogin(msg);
                break;
            case CHATMESSAGE:
                Message chatMessage = gson.fromJson(msg.getContent(), Message.class);
                canvas.showChatMessage(chatMessage);
                break;
            case POINT:
                LinePoints points = gson.fromJson(msg.getContent(), LinePoints.class);
                canvas.drawRemotePoint(points.getLastX(), points.getLastY(), points.getX(), points.getY(), points.getColor(), points.getBrushSize());
                break;
            case CLEARCANVAS:
                canvas.clear();
                break;
            case LOBBYCREATE:
                lobby = gson.fromJson(msg.getContent(), Lobby.class);
                success = (lobby.getId() != 0);
                lobbyCreateController.updateLobbyCreated(success, lobby);
                break;
            case REQUESTLOBBIES:
                LobbyShell[] lobbies = gson.fromJson(msg.getContent(), LobbyShell[].class);

                // Temp fix for having lobby shells without token
                // todo: manage this serverside
                for (LobbyShell shell : lobbies) {
                    shell.clearToken();
                }

                lobbyOverviewController.updateLobbyOverview(lobbies);
                break;
            case LOBBYJOIN:
                lobby = gson.fromJson(msg.getContent(), Lobby.class);
                success = (lobby.getId() != 0);
                lobbyOverviewController.closeLobbyOverview(success, lobby);
                break;
            case LOBBYLEAVE:
                lobby = null;
                leavingPlayer = gson.fromJson(msg.getContent(), Player.class);
                lobbyController.updatePlayerStatus(leavingPlayer, false);
                break;
            case NEWPLAYERJOINED:
                Player player = gson.fromJson(msg.getContent(), Player.class);
                lobbyController.updatePlayerStatus(player, true);
                break;
            case LOBBYJOINERROR:
                lobbyOverviewController.showLobbyJoinError(msg.getContent());
                break;
            case GAMESTART:
                lobby = null;
                game = gson.fromJson(msg.getContent(), Game.class);
                lobbyController.updateGameStarted(game);

                drawerName = game.getRounds().get(0).getDrawerName();
                myName = localPlayer.getUsername();

                if(myName.equals(drawerName)) {
                    isDrawer = true;
                } else {
                    isDrawer = false;
                }
                break;
            case PICKWORD:
                PickWordEvent pickWordEvent = gson.fromJson(msg.getContent(), PickWordEvent.class);
                wordsToPick = pickWordEvent.newWords;
                if(canvas != null) {
                    canvas.showWords(wordsToPick);
                }
                break;
            case ROUNDUPDATE:
                canvas.setCanDraw(false);

                Round round = gson.fromJson(msg.getContent(), Round.class);
                game.addNewRound(round);

                drawerName = round.getDrawerName();
                myName = localPlayer.getUsername();

                if (drawerName.equals(myName)) {
                    isDrawer = true;
                }
                else {
                    isDrawer = false;
                }

                canvas.updateCurrentDrawerName(drawerName);
                canvas.roundStart(-1);
                break;
            case WORDCHOSEN:
                String time = gson.fromJson(msg.getContent(), String.class);
                canvas.roundStart(Integer.parseInt(time));

                if(isDrawer) {
                    canvas.setCanDraw(true);
                }
                break;
            case LEADERBOARDUPDATE:
                LeaderboardEntry leaderboardEntry = gson.fromJson(msg.getContent(), LeaderboardEntry.class);
                canvas.guessedWord(leaderboardEntry);
                canvas.updateLeaderboard(leaderboardEntry);
                break;
            case GAMELEAVE:
                game = null;
                wordsToPick = null;
                isDrawer = false;
                leavingPlayer = gson.fromJson(msg.getContent(), Player.class);
                canvas.playerLeftEvent(leavingPlayer);
                break;
            case ENDGAME:
                game = null;
                wordsToPick = null;
                isDrawer = false;
                canvas.updateEndGame();
                break;
            case ROUNDWORD:
                String roundWord = msg.getContent();
                canvas.showRoundWord(roundWord);
                break;
            case WORDALMOSTCORRECT:
                canvas.showGuessAlmostCorrect(msg.getContent());
                break;
        }
    }

    //Set Controllers Methods
    public void setDrawerController(IDrawerCanvasController canvas) {
        this.canvas = canvas;
        if (wordsToPick != null) {
            canvas.showWords(wordsToPick);
        }
    }

    public void setRegisterController(IRegisterController register) {
        this.register = register;
    }

    public void setLobbyCreateController(ILobbyCreateController lobbyCreateController) {
        this.lobbyCreateController = lobbyCreateController;
    }

    public void setLobbyOverviewController(ILobbyOverviewController lobbyOverviewController) {
        this.lobbyOverviewController = lobbyOverviewController;
    }

    public void setLobbyController(ILobbyController lobbyController) {
        this.lobbyController = lobbyController;
    }

    public void setLogin(ILoginController login) {
        this.login = login;
    }

    //Handle Methods

    private void handleRegister(WebSocketMessage msg) {
        Player player = gson.fromJson(msg.getContent(), Player.class);
        boolean isSucces = false;
        if (player != null) {
            isSucces = true;
            register.updateRegisterToLogin();
        }
        
        if (!isSucces) {
            ScreenLoader.getInstance().displayAlert("Username Already Taken", "The given username was already taken", Alert.AlertType.ERROR);
        }

        log.info("The register attempt was " + isSucces);
    }

    public void handleLogin(WebSocketMessage msg) {
        AccountDataEvent accountDataEvent = gson.fromJson(msg.getContent(), AccountDataEvent.class);
        boolean isSucces = false;
        if (accountDataEvent.player != null) {
            isSucces = true;
            localPlayer = accountDataEvent.player;
            login.updateLoginToLobbyOverview();
        }
        if (!isSucces) {
            login.enableLoginButton();
            login.showError();
        }

        log.info("The login attempt was " + isSucces);
    }

    public void handleLogout(WebSocketMessage msg) {
        Player player = gson.fromJson(msg.getContent(), Player.class);
        boolean isSucces = false;
        if (player != null) {
            isSucces = true;
        }

        // TODO: update UI (Interface)

        log.info("The logout attempt was " + isSucces);
    }

    public void retryConnection() throws NullPointerException {
        startClient();
    }

    public boolean isConnected() {
        return session != null;
    }
}
