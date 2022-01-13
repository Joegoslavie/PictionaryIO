package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LobbyTest {
    Lobby lobby;
    LobbyShell lobbyShell;
    boolean exceptionThrown;

    @BeforeEach
    public void setUp(){
        lobbyShell = new LobbyShell(3, "lobbyShell", false, 3);
        lobby = new Lobby(lobbyShell);
        lobby.addPlayer(new Player("name1"));
        lobby.addPlayer(new Player("name2"));
        exceptionThrown = false;
    }

    //constructor and getters/setters tests
    @Test
    public void constructorTest(){
        Lobby lobby = null;
        try{
            lobby = new Lobby(new LobbyShell());
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }


    //method tests
    @Test
    public void removePlayerTest() {
        int playersBefore = lobby.getPlayers().size();
        int playersAfter = -1;
        try{
            lobby.removePlayer(lobby.getPlayers().get(0));
            playersAfter = lobby.getPlayers().size();
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertEquals(playersBefore, playersAfter + 1);
    }

    @Test
    public void addPlayerTest() {
        int playersBefore = lobby.getPlayers().size();
        int playersAfter = -1;
        try{
            lobby.addPlayer(new Player("lolol"));
            playersAfter = lobby.getPlayers().size();
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertEquals(playersBefore, playersAfter - 1);
    }
}