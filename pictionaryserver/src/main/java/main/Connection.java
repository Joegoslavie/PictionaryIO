package main;

import com.google.gson.Gson;
import enums.Header;
import enums.RoundState;
import interfaces.IConnection;
import models.*;
import observerpattern.IObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import other.Utils;
import rest.RestClient;
import restmodels.RESTPlayer;
import serverInterfaces.IServerConnection;
import webSocketMessages.AccountDataEvent;
import webSocketMessages.PickWordEvent;
import webSocketMessages.WebSocketMessage;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@ServerEndpoint(value = "/communicator/")
public class Connection implements IConnection, IObserver, IServerConnection {
    private static final ArrayList<Session> sessions = new ArrayList<>();
    private static final ArrayList<Lobby> lobbies = new ArrayList<>();
    private static final ArrayList<Game> games = new ArrayList<>();
    private static final Gson gson = new Gson();
    private static final Logger log = LoggerFactory.getLogger(Connection.class);

    private static final Map<Session, Lobby> lobbyMap = new HashMap<>();
    private static final Map<Session, Game> gameMap = new HashMap<>();

    private Timer lobbyCreateTimer;

    private static int lobbyCount = 1;

    private static final int RANDOM_WORD_COUNT = 3;

    private Player localPlayer;
    private Session localSession;

    public static void setLobbyCount(int lobbyCount) {
        Connection.lobbyCount = lobbyCount;
    }

    //voor tests
    public void setLocalPlayer(Player localPlayer) {
        this.localPlayer = localPlayer;
    }

    public void putEntryInGameMap(Session session, Game game){
        gameMap.put(session, game);
    }

    @OnOpen
    public void onOpen(Session session) {
        localSession = session;
        sessions.add(session);

        log.info(getFormattedString("New client connected: %s", session));
    }

    @OnClose
    public void onClose(CloseReason closeReason, Session session) {

        handleLeaveLobbyMessage(session);
        if (localPlayer != null) {
            RestClient.getInstance().signOut(localPlayer.getUsername()); // Sign out the disconnecting user
            localPlayer = null;
        }

        localSession = null;
        sessions.remove(session);

        log.info(getFormattedString("Client closed: %s", session));
    }

    public String getFormattedString(String format, Object ... args) {
        Formatter formatter = new Formatter();
        formatter.format(format, args);
        String str = formatter.toString();
        formatter.close();

        return str;
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        WebSocketMessage msgObj = gson.fromJson(message, WebSocketMessage.class);

        Header header = msgObj.getHeader();
        log.info(getFormattedString("Message Received: %s", header.toString()));

        switch(header){
            case REGISTER:
                AccountDataEvent accountDataEventRegister = gson.fromJson(msgObj.getContent(), AccountDataEvent.class);
                handleRegisterMessage(accountDataEventRegister, session);
                break;
            case CHATMESSAGE:
                handleChatMessage(msgObj);
                break;
            case LOGIN:
                AccountDataEvent accountDataEventLogin = gson.fromJson(msgObj.getContent(), AccountDataEvent.class);
                handleLoginMessage(accountDataEventLogin, session);
                break;
            case LOGOUT:
                AccountDataEvent accountDataEventLogout = gson.fromJson(msgObj.getContent(), AccountDataEvent.class);
                handleLogoutMessage(accountDataEventLogout, session);
                break;
            case REQUESTLOBBIES:
                for(Lobby lobby : lobbies) {
                    lobby.setAmountOfJoinedPlayers(lobby.getPlayers().size());
                }
                WebSocketMessage returnLobbiesMessage = new WebSocketMessage(Header.REQUESTLOBBIES, gson.toJson(lobbies));
                sendMessage(returnLobbiesMessage, session);
                break;
            case LOBBYCREATE:
                LobbyShell lobbyShell = gson.fromJson(msgObj.getContent(), LobbyShell.class);
                handleCreateLobbyMessage(lobbyShell, localPlayer);
                break;
            case LOBBYLEAVE:
                handleLeaveLobbyMessage(session);
                break;
            case LOBBYJOIN:
                LobbyShell lobbyShel1 = gson.fromJson(msgObj.getContent(), LobbyShell.class);
                handleJoinLobbyMessage(lobbyShel1, session);
                break;
            case SEARCHLOBBY:
                String keyword = gson.fromJson(msgObj.getContent(), String.class);
                handleSearchLobbyMessage(keyword, session);
                break;
            case GAMESTART:
                handleGameStartMessage();
                break;
            case GAMELEAVE:
                handleLeaveGame();
                break;
            case PICKWORD:
                PickWordEvent pickWordEvent = gson.fromJson(msgObj.getContent(), PickWordEvent.class);
                handlePickWordMessage(pickWordEvent, session);
                break;
            case POINT:
                Game localGame = gameMap.get(localSession);

                String newDrawerName = localGame.getCurrentRound().getDrawerName();
                String localName = localPlayer.getUsername();
                if(newDrawerName.equals(localName)) {
                    sendMessageToPlayers(localGame.getPlayers(), msgObj);
                }
                break;
            case CLEARCANVAS:
                Game game = gameMap.get(localSession);
                String drawerName = game.getCurrentRound().getDrawerName();
                if(localPlayer.getUsername().equals(drawerName)) {
                    sendMessageToPlayers(gameMap.get(localSession).getPlayers(), msgObj);
                }
                break;
            default:
                break;
        }
    }

    private void handleLeaveGame(){
        Game game = gameMap.get(localSession);
        if(game != null){
            sendMessageToPlayers(game.getPlayers(), new WebSocketMessage(Header.GAMELEAVE, gson.toJson(localPlayer)));

            String localName = localPlayer.getUsername();
            String drawerName = game.getCurrentRound().getDrawerName();
            boolean isDrawer = localName.equals(drawerName);

            game.getPlayers().remove(localPlayer);
            ArrayList<LeaderboardEntry> tempEntries = new ArrayList<>();

            if(isDrawer) {
                game.setCurrentPlayerIndex(game.getCurrentPlayerIndex() - 1);
            }

            int playerCount = game.getPlayers().size();
            Player lastPlayer = game.getPlayers().get(game.getPlayers().size() - 1);
            if(lastPlayer.getTurnCount() == game.getAmountOfTurnsPerPlayer() || game.getPlayers().isEmpty() || playerCount == 1) {
                endGame(game);
            }
            else if(isDrawer) {
                startNewRound(game);
            }

            for(LeaderboardEntry entry : game.getLeaderboardEntries()){
                if(entry.getUsername().toLowerCase().equals(localPlayer.getUsername().toLowerCase())){
                    tempEntries.add(entry);
                }
            }

            game.getLeaderboardEntries().removeAll(tempEntries);
        }
    }

    private void handleChatMessage(WebSocketMessage msg) {
        if(!localPlayer.hasGuessedWord()) {
            Message incomingMessage = gson.fromJson(msg.getContent(), Message.class);
            String wordGuess = incomingMessage.getMessage();
            Game game = gameMap.get(localSession);


            List<Round> rounds = game.getRounds();
            Round currentRound = rounds.get(rounds.size() - 1);
            String wordToDraw = currentRound.getWordToDraw();

            String wordToDrawLowerCast = wordToDraw.toLowerCase();
            String wordGuessLowerCase = wordGuess.toLowerCase();

            if (wordToDraw != null && wordToDrawLowerCast.equals(wordGuessLowerCase)) {
                updateLeaderboardEntry(game);

                // Update the localPlayer.guessedWord after the updateLeaderboardEntry function
                localPlayer.setGuessedWord(true);

                // Check if all players have guessed the word and the round should be ended
                List<Player> players = game.getPlayers();
                boolean allPlayersGuessed = true;
                for (Player player : players) {
                    if (!player.hasGuessedWord()) {
                        allPlayersGuessed = false;
                        break;
                    }
                }

                if (allPlayersGuessed) {
                    Player lastPlayer = game.getPlayers().get(game.getPlayers().size() - 1);
                    if(lastPlayer.getTurnCount() == game.getAmountOfTurnsPerPlayer())
                    {
                        endGame(game);
                    }
                    else {
                        startNewRound(game);
                    }
                }
            } else if(wordToDraw != null && Utils.calculateEditDistance(wordGuess, wordToDraw) <= 2) {
                WebSocketMessage message = new WebSocketMessage(Header.WORDALMOSTCORRECT, wordGuess);
                sendMessage(message, localSession);
            } else {
                sendMessageToPlayers(game.getPlayers(), msg);
            }
        }
    }

    private void endGame(Game game) {
        game.stopRoundTimer();

        // Reset guessed word when game ends, as player stays for next game
        for(Player player : game.getPlayers()) {
            player.setGuessedWord(false);
            player.setTurnCount(0);
        }

        // Let clients know that game has ended
        WebSocketMessage msg = new WebSocketMessage(Header.ENDGAME, null);
        sendMessageToPlayers(game.getPlayers(), msg);

        // Create the new lobby and send it
        // Wait 5 seconds
        lobbyCreateTimer = new Timer();
        lobbyCreateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                int lobbySize = game.getPlayers().size();
                if(lobbySize > 0) {
                    if (lobbySize == 1) {
                        lobbySize += 1;
                    }
                    LobbyShell newShell = new LobbyShell(lobbySize, game.getLobbySettings());
                    createNewLobby(newShell, game.getPlayers());
                }

                stopLobbyCreateTimer();
            }
        }, 10000);

        // dispose game
        for(Player player : game.getPlayers()) {
            gameMap.remove(player.getSession());
        }
    }

    private void stopLobbyCreateTimer() {
        lobbyCreateTimer.purge();
        lobbyCreateTimer.cancel();
    }

    private void handlePickWordMessage(PickWordEvent pickWordEvent, Session session) {
        if(pickWordEvent.chosenWord == null) {
            RandomWordPicker randomWordPicker = null;
            try {
                randomWordPicker = new RandomWordPicker();
            } catch (IOException e) {
                log.error("Error occurred during word loading!");
                return;
            }

            pickWordEvent.newWords = randomWordPicker.getRandomWords(RANDOM_WORD_COUNT);

            String pickWordEventStr = gson.toJson(pickWordEvent);
            WebSocketMessage newMessage = new WebSocketMessage(Header.PICKWORD, pickWordEventStr);
            sendMessage(newMessage, session);
        } else {
            Game game = gameMap.get(localSession);

            Round currentRound = game.getCurrentRound();
            currentRound.setRoundState(RoundState.STARTED);
            currentRound.setWordToDraw(pickWordEvent.chosenWord);
            
            game.startRoundTimer();

            // Do NOT send the round to all the clients as it contains the word to draw

            String roundTimeStr = gson.toJson(game.getRoundTime());
            WebSocketMessage newMessage = new WebSocketMessage(Header.WORDCHOSEN, roundTimeStr);

            List<Player> players = game.getPlayers();
            sendMessageToPlayers(players, newMessage);
        }
    }

    private void handleRegisterMessage(AccountDataEvent accountDataEventRegister, Session session) {
        Player newPlayerRegister = register(accountDataEventRegister.username, accountDataEventRegister.password);
        if(newPlayerRegister != null) {
            accountDataEventRegister.player = newPlayerRegister;
        }

        String accountDataRegisterStr = gson.toJson(accountDataEventRegister);
        WebSocketMessage returnRegisterMessage = new WebSocketMessage(Header.REGISTER, accountDataRegisterStr);
        sendMessage(returnRegisterMessage, session);
    }

    private void handleLoginMessage(AccountDataEvent accountDataEventLogin, Session session) {
        Player newPlayerLogin = login(accountDataEventLogin.username, accountDataEventLogin.password);
        if(newPlayerLogin != null){
            accountDataEventLogin.player = newPlayerLogin;
            localPlayer = newPlayerLogin;
            localPlayer.setSession(session);
        }

        String accountDataLoginStr = gson.toJson(accountDataEventLogin);
        WebSocketMessage returnLoginMessage = new WebSocketMessage(Header.LOGIN, accountDataLoginStr);
        sendMessage(returnLoginMessage, session);
    }

    private void handleLogoutMessage(AccountDataEvent accountDataEventLogout, Session session) {
        if(localPlayer != null){
            accountDataEventLogout.player = localPlayer;
            localPlayer = null;
        }

        String accountDataLogoutStr = gson.toJson(accountDataEventLogout);
        WebSocketMessage returnLogoutMessage = new WebSocketMessage(Header.LOGOUT, accountDataLogoutStr);
        sendMessage(returnLogoutMessage, session);
    }

    private void handleSearchLobbyMessage(String keyword, Session session) {
        throw new UnsupportedOperationException("handleSearchLobbyMessage not implemented.");
    }

    private void handleJoinLobbyMessage(LobbyShell lobbyShel1, Session session) {
        Lobby lobby = getLobbyFromId(lobbyShel1.getId());
        if (lobby == null) {
            WebSocketMessage message = new WebSocketMessage(Header.LOBBYJOINERROR, "Lobby not found");
            sendMessage(message, session);
            return;
        }

        // If private check the token
        if(lobby.isPrivate() && !lobby.getToken().equals(lobbyShel1.getToken())) {
            WebSocketMessage message = new WebSocketMessage(Header.LOBBYJOINERROR, "Incorrect token");
            sendMessage(message, session);
            return;
        }

        //Check if there is space for the player to join
        if(lobby.getPlayers().size() == lobby.getNumPlayers()) {
            WebSocketMessage message = new WebSocketMessage(Header.LOBBYJOINERROR, "No more room in this lobby");
            sendMessage(message, session);
            return;
        }

        lobby.addPlayer(localPlayer);
        lobbyMap.put(session, lobby);

        for (Player p : lobby.getPlayers()) {
            if (p.getUsername().equals(localPlayer.getUsername())) {
                WebSocketMessage message = new WebSocketMessage(Header.LOBBYJOIN, gson.toJson(lobby));
                sendMessage(message, p.getSession());
            } else {
                WebSocketMessage message = new WebSocketMessage(Header.NEWPLAYERJOINED, gson.toJson(localPlayer));
                sendMessage(message, p.getSession());
            }
        }
    }

    private void handleLeaveLobbyMessage(Session session) {

        // If true the player is currently in a lobby
        if (lobbyMap.containsKey(session)) {

            // Get lobby of current user.
            Lobby currentLobby = lobbyMap.get(session);
            currentLobby.removePlayer(localPlayer); // Remove player from this lobby
            if (currentLobby.getPlayers().isEmpty()) { // If playercount of lobby equals zero we ditch it
                lobbies.remove(currentLobby);
            }

            lobbyMap.remove(session); // Remove reference of player
            WebSocketMessage playerLeftLobbyMessage = new WebSocketMessage(Header.LOBBYLEAVE, gson.toJson(localPlayer));

            List<Player> players = currentLobby.getPlayers();
            for (Player player : players) {
                sendMessage(playerLeftLobbyMessage, player.getSession());
            }
        }
    }

    private void handleGameStartMessage() {
        Lobby localLobby = lobbyMap.get(localSession);

        // Game being setup
        Game newGame = new Game(localLobby.getName(), localLobby);
        newGame.setiConnection(this);
        newGame.addObserver(this);

        // The Lobby object is destroyed by removing the references so that it can later be recreated with the newGame.lobbySettings
        lobbyMap.remove(localSession);
        lobbies.remove(localLobby);

        gameMap.put(localSession, newGame);
        for(Player player : localLobby.getPlayers()) {
            // All players need a map to the game they are in, not just the player that creates the game
            gameMap.put(player.getSession(), newGame);
        }
        games.add(newGame);

        Player gameAdmin = newGame.getPlayers().get(0);
        gameAdmin.incrementTurnCount();

        String newGameStr = gson.toJson(newGame);
        WebSocketMessage newMessage = new WebSocketMessage(Header.GAMESTART, newGameStr);
        sendMessageToPlayers(newGame.getPlayers(), newMessage);

        sendRandomWordsToPick(gameAdmin.getSession());
    }

    @OnError
    public void onError(Throwable cause, Session session) {
        String firstErrorStr = String.format("New error: %s", session.getRequestURI());
        log.error(firstErrorStr);

        String secondErrorStr = String.format("Exception: %s", cause.toString());
        log.error(secondErrorStr);
    }

    @Override
    public Player login(String username, String password) {
        RestClient restClient = RestClient.getInstance();
        RESTPlayer restPlayer = restClient.signIn(username, password);

        Player player = null;
        if (restPlayer != null) {
            player = new Player(restPlayer.getUsername());
        }

        return player;
    }

    @Override
    public Player register(String username, String password) {
        RestClient restClient = RestClient.getInstance();
        RESTPlayer restPlayer = restClient.register(username, password);

        Player player = null;
        if (restPlayer != null) {
            player = new Player(restPlayer.getUsername());
        }

        return player;
    }

    @Override
    public void sendMessage(WebSocketMessage message, Session session) {
        String messageStr = gson.toJson(message);
        session.getAsyncRemote().sendText(messageStr);
    }

    @Override
    public void sendMessage(WebSocketMessage message) {
        throw new UnsupportedOperationException("sendMessage not implemented yet");
    }

    @Override
    public void logout() {
        // TODO, handle logout
        throw new UnsupportedOperationException("logout not implemented yet");
    }

    private void updateLeaderboardEntry(Game game) {
        List<LeaderboardEntry> leaderboardEntries = game.getLeaderboardEntries();
        LeaderboardEntry entryToUpdate = null;
        for(LeaderboardEntry entry : leaderboardEntries) {
            if(entry.getUsername().equals(localPlayer.getUsername())) {
                entryToUpdate = entry;
                break;
            }
        }

        if(entryToUpdate == null) {
            throw new IllegalArgumentException("[updateLeaderboardEntry] - The local players (" + localPlayer.getUsername() + ") LeaderboardEntry could not be found.");
        }

        int guessedTime = game.getRoundTimerCounter();
        int roundTime = game.getRoundTime();
        int startPlayerCount = game.getStartPlayerCount();

        int playerPosition = 0;
        for(Player player : game.getPlayers()) {
            if(player.hasGuessedWord()) {
                playerPosition += 1;
            }
        }

        int newScore = entryToUpdate.getScore();
        newScore += roundTime - ((playerPosition - 1) * (roundTime / startPlayerCount));
        newScore += roundTime - guessedTime;
        entryToUpdate.setScore(newScore);

        String entryToUpdateStr = gson.toJson(entryToUpdate);
        WebSocketMessage newMessage = new WebSocketMessage(Header.LEADERBOARDUPDATE, entryToUpdateStr);
        sendMessageToPlayers(game.getPlayers(), newMessage);
    }

    private void startNewRound(Game game) {
        game.stopRoundTimer();

        Round currentRound = game.getCurrentRound();
        String wordToDraw = currentRound.getWordToDraw();
        WebSocketMessage roundWordMsg = new WebSocketMessage(Header.ROUNDWORD, wordToDraw);
        sendMessageToPlayers(game.getPlayers(), roundWordMsg);

        currentRound.setRoundState(RoundState.FINISHED);
        game.startNewRound();

        WebSocketMessage clearCanvasMessage = new WebSocketMessage(Header.CLEARCANVAS, null);
        sendMessageToPlayers(game.getPlayers(), clearCanvasMessage);

        Round newRound = game.getCurrentRound();
        String roundStr = gson.toJson(newRound);
        WebSocketMessage newGameMessage = new WebSocketMessage(Header.ROUNDUPDATE, roundStr);
        sendMessageToPlayers(game.getPlayers(), newGameMessage);

        Player currentDrawer = game.getPlayers().get(game.getCurrentPlayerIndex());
        sendRandomWordsToPick(currentDrawer.getSession());
    }

    private void sendRandomWordsToPick(Session session) {
        RandomWordPicker randomWordPicker = null;
        try {
            randomWordPicker = new RandomWordPicker();
        } catch (IOException e) {
            log.error("Error occurred during word loading!");
            return;
        }

        ArrayList<String> randomWordList = randomWordPicker.getRandomWords(RANDOM_WORD_COUNT);
        PickWordEvent pickWordEvent = new PickWordEvent(randomWordList, null);

        String pickWordEventStr = gson.toJson(pickWordEvent);
        WebSocketMessage newMessage2 = new WebSocketMessage(Header.PICKWORD, pickWordEventStr);
        sendMessage(newMessage2, session);
    }

    private void createNewLobby(LobbyShell lobbyShell, List<Player> players) {
        String lobbyName = lobbyShell.getName();
        for (Lobby lobby : lobbies) {
            if (lobby.getName().equals(lobbyName)) {
                return;
            }
        }

        Lobby newLobby = new Lobby(lobbyShell);
        if (newLobby.isPrivate()) {
            newLobby.setToken(Utils.generateToken(5));
        }

        newLobby.setId(lobbyCount);
        setLobbyCount(lobbyCount + 1);
        lobbies.add(newLobby);

        for (Player p : players) {
            newLobby.addPlayer(p);
            lobbyMap.put(p.getSession(), newLobby);
        }

        for (Player p : players) {
            WebSocketMessage message = new WebSocketMessage(Header.LOBBYJOIN, gson.toJson(newLobby));
            sendMessage(message, p.getSession());
        }
    }

    private Lobby handleCreateLobbyMessage(LobbyShell shell, Player player){
        boolean lobbyNameExists = false;

        String lobbyName = shell.getName();
        for (Lobby lobby : lobbies) {
            if (lobby.getName().equals(lobbyName)) {
                lobbyNameExists = true;
                break;
            }
        }

        Lobby newLobby = null;
        if (!lobbyNameExists) {
            newLobby = new Lobby(shell);
            if(newLobby.isPrivate())
                newLobby.setToken(Utils.generateToken(5));

            newLobby.setId(lobbyCount);
            setLobbyCount(lobbyCount + 1);
            newLobby.addPlayer(player);
            lobbies.add(newLobby);
            lobbyMap.put(player.getSession(), newLobby);
        }

        String lobbyStr = gson.toJson(newLobby);
        WebSocketMessage msg = new WebSocketMessage(Header.LOBBYCREATE, lobbyStr);
        sendMessage(msg, player.getSession());
        return newLobby;
    }

    private Lobby getLobbyFromId(int id) {
        for (Lobby lobby : lobbies) {
            if (lobby.getId() == id) {
                return lobby;
            }
        }
        return null;
    }

    private void sendMessageToPlayers(List<Player> players, WebSocketMessage msg) {
        for(Player player : players) {
            sendMessage(msg, player.getSession());
        }
    }

    @Override
    public void update() {
        Game localGame = gameMap.get(localSession);

        Player lastPlayer = localGame.getPlayers().get(localGame.getPlayers().size() - 1);
        if(lastPlayer.getTurnCount() == localGame.getAmountOfTurnsPerPlayer())
        {
            endGame(localGame);
        }
        else {
            startNewRound(localGame);
        }
    }
}