package models;

import enums.GameState;
import interfaces.IConnection;
import observerpattern.IObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Game {
    private String gameName;
    private int currentPlayerIndex;
    private GameState gameState;
    private ArrayList<LeaderboardEntry> leaderboardEntries;
    private ArrayList<Round> rounds;
    private ArrayList<Player> players;
    private ArrayList<Message> messages;
    private transient IConnection iConnection;
    private LobbySettings lobbySettings;
    private int amountOfTurnsPerPlayer;
    private int roundTime = 60;
    private int startPlayerCount;

    private Timer roundTimer;
    private int roundTimerCounter = 0;

    private transient ArrayList<IObserver> observers;

    public Round getCurrentRound() {
        if(rounds != null && rounds.size() > 0) {
            return rounds.get(rounds.size() - 1);
        }

        return null;
    }

    public LobbySettings getLobbySettings(){
        return this.lobbySettings;
    }

    public int getStartPlayerCount() {
        return startPlayerCount;
    }

    public int getRoundTime() {
        return roundTime;
    }

    public int getRoundTimerCounter() {
        return roundTimerCounter;
    }

    public List<Player> getPlayers() {
        // TODO, make it readonly
        return this.players;
    }

    public String getGameName() {
        return this.gameName;
    }

    public int getCurrentPlayerIndex() {
        return this.currentPlayerIndex;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public List<LeaderboardEntry> getLeaderboardEntries() {
        return this.leaderboardEntries;
    }

    public List<Round> getRounds() {
        return this.rounds;
    }

    public int getAmountOfTurnsPerPlayer() {
        return this.amountOfTurnsPerPlayer;
    }

    public void setCurrentPlayerIndex(int index) {
        this.currentPlayerIndex = index;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setiConnection(IConnection iConnection) {
        this.iConnection = iConnection;
    }

    public Game(String gameName, Lobby lobby) {
        // Do the setup for the Game object such that it can be sent
        this.observers = new ArrayList();

        this.gameName = gameName;
        lobbySettings = new LobbySettings(lobby.isPrivate(), lobby.getName(), lobby.getAmountOfTurnsPerPlayer());
        this.gameState = GameState.GAME;

        leaderboardEntries = new ArrayList<>();
        players = new ArrayList<>();
        messages = new ArrayList<>();

        for(Player player : lobby.getPlayers()) {
            leaderboardEntries.add(new LeaderboardEntry(player.getUsername(), 0));
            players.add(player);
        }

        currentPlayerIndex = 0;
        startPlayerCount = players.size();
        rounds = new ArrayList<Round>();
        //Set guessedWord so the drawer won't have to guess the word to continue to the next round
        players.get(0).setGuessedWord(true);
        rounds.add(new Round(players.get(0).getUsername()));

        this.amountOfTurnsPerPlayer = lobby.getAmountOfTurnsPerPlayer();
    }

    public void addLeaderboardEntry(String username, int score) {

    }

    public void setLeaderboardEntry(String username, int score) {

    }

    public void sendChatMessage(String msg) {

    }

    public void AddMessageToChat(Message msg) {

    }

    public void startNewRound() {
        //Reset the player guessed words
        for(Player p : players) {
            p.setGuessedWord(false);
        }

        currentPlayerIndex += 1;
        if(currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0;
        }
        Player newCurrentPlayer = players.get(currentPlayerIndex);
        newCurrentPlayer.incrementTurnCount();
        //Set guessedWord so the drawer won't have to guess the word to continue to the next round
        newCurrentPlayer.setGuessedWord(true);

        Round newRound = new Round(newCurrentPlayer.getUsername());
        rounds.add(newRound);
    }

    public void startRoundTimer() {
        roundTimerCounter = 0;
        roundTimer = new Timer();
        roundTimer.schedule(new TimerTask() {
           @Override
           public void run() {
               roundTimerCounter += 1;
               if(roundTimerCounter >= roundTime) {
                   for(IObserver observer : observers) {
                       observer.update();
                   }

                   stopRoundTimer();
               }
           }
        }, 5, 1000);
    }

    public void stopRoundTimer() {
        if(roundTimer != null) {
            roundTimer.cancel();
        }
    }

    public void addObserver(IObserver observer) {
        observers.add(observer);
    }

    public void addNewRound(Round round) {
        rounds.add(round);
    }
}