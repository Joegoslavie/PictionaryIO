package models;

public class LobbySettings {
    private boolean isPrivate;
    private String name;
    private int turnCount;

    public boolean getIsPrivate() {
        return this.isPrivate;
    }

    public String getName() {
        return this.name;
    }

    public int getTurnCount() {
        return this.turnCount;
    }

    public LobbySettings(boolean isPrivate, String name, int turnCount) {
        this.isPrivate = isPrivate;
        this.name = name;
        this.turnCount = turnCount;
    }
}
