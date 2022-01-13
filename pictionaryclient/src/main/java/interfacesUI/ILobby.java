package interfacesUI;

public interface ILobby {
    void addPlayer(String playerName);
    void removePlayer(int playerListPos);
    void removePlayer(String playerName);
    void setLobbyName(String newLobbyName);
    void setRoomToken(String newRoomToken);
    void startGame();
}