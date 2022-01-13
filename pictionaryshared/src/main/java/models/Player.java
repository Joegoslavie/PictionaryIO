package models;

import javax.websocket.Session;

public class Player {
    private String username;
    private int localScore;
    private boolean guessedWord;
    private transient Session session;
    private int turnCount;

    public int getTurnCount() {
        return this.turnCount;
    }

    public Player(){}
    public Player(String username) {
        this.username = username;
    }

    public void setLocalScore(int localScore) {
        this.localScore = localScore;
    }

    public void setGuessedWord(boolean guessedWord) {
        this.guessedWord = guessedWord;
    }

    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    public String getUsername() {
        return username;
    }

    public int getLocalScore() {
        return localScore;
    }

    public boolean hasGuessedWord() {
        return guessedWord;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return this.session;
    }

    public void incrementTurnCount() {
        turnCount += 1;
    }

    @Override
    public String toString() {
        return this.username;
    }
}
