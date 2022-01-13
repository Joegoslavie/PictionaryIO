package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lobby extends LobbyShell{
    private ArrayList<Player> players = new ArrayList<>();
    private LobbyShell lobbyShell;

    public Lobby(LobbyShell lobbyShell) {
        super(lobbyShell.getNumPlayers(), lobbyShell.getName(), lobbyShell.isPrivate(), lobbyShell.getAmountOfTurnsPerPlayer());
        if(this.isPrivate()) {
            this.token = lobbyShell.getToken();
        }
        this.lobbyShell = lobbyShell;
    }

    public boolean removePlayer(Player player) {
        Player toRemovePlayer = null;
        for(Player p : players){
            if(p.getUsername().equals(player.getUsername())){
                toRemovePlayer = p;
                break;
            }
        }

        if(toRemovePlayer != null) {
            players.remove(toRemovePlayer);
            return true;
        }
        return false;
    }

    public boolean addPlayer(Player player) {
        if(this.players.size() < getNumPlayers()) {
            this.players.add(player);
            return true;
        }
        return false;
    }

    public LobbyShell getShell() {
        return lobbyShell;
    }

    public void setId(int id){
        super.setId(id);
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(this.players);
    }
}
