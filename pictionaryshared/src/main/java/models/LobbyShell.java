package models;

public class LobbyShell {
    private int numPlayers;
    private String name;
    private boolean isPrivate;
    private int id;
    private int amountOfTurnsPerPlayer;
    private int amountOfJoinedPlayers;
    protected String token;

    public LobbyShell(int numPlayers, String name, boolean isPrivate, int amountOfTurnsPerPlayer) {
        this.numPlayers = numPlayers;
        this.name = name;
        this.isPrivate = isPrivate;
        this.amountOfTurnsPerPlayer = amountOfTurnsPerPlayer;
    }

    public LobbyShell(int numPlayers, LobbySettings settings){
        this.numPlayers = numPlayers;
        this.name = settings.getName();
        this.isPrivate = settings.getIsPrivate();
        this.amountOfTurnsPerPlayer = settings.getTurnCount();
    }

    public LobbyShell() {

    }

    public void join(Player player) {

    }

    public void join(Player player, String token) {

    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public String getName() {
        return name;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public int getId() {
        return id;
    }

    public int getAmountOfTurnsPerPlayer(){
        return amountOfTurnsPerPlayer;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void clearToken(){
        this.token = "";
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        String name = this.name + " - " + this.id + " - " + this.amountOfJoinedPlayers + "/" + this.numPlayers + " players";
        if(this.isPrivate) {
            name += " - Private";
        }
        return name;
    }

    public void setAmountOfJoinedPlayers(int amountOfJoinedPlayers) {
        this.amountOfJoinedPlayers = amountOfJoinedPlayers;
    }
}
