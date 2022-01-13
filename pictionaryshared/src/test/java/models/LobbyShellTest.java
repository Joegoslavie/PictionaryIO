package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class LobbyShellTest {
    LobbyShell lobbyShell;
    boolean exceptionThrown;

    @BeforeEach
    public void setUp(){
        lobbyShell = new LobbyShell(2, "name", false, 3);
        exceptionThrown = false;
    }

    //constructors (and getters/setters)
    @Test
    public void constructorTest(){
        LobbyShell lobbyShell = null;
        try{
            lobbyShell = new LobbyShell(2, "name", false, 3);
            lobbyShell = new LobbyShell();
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void gettersTest(){
        String name = null;
        int numPlayers = -1;
        boolean privé = true;
        int id = -1;
        int amountOfTurnsPerPlayer = -1;
        String token = null;
        try{
            lobbyShell.setId(1);
            lobbyShell.setToken("token");

            name = lobbyShell.getName();
            numPlayers = lobbyShell.getNumPlayers();
            privé = lobbyShell.isPrivate();
            id = lobbyShell.getId();
            amountOfTurnsPerPlayer = lobbyShell.getAmountOfTurnsPerPlayer();
            token = lobbyShell.getToken();
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertEquals(name, "name");
        assertEquals(numPlayers, 2);
        assertEquals(privé, false);
        assertEquals(id, 1);
        assertEquals(amountOfTurnsPerPlayer, 3);
        assertEquals(token, "token");
    }

    @Test
    public void toStringTest(){
        try{
            String lobbyShellToString = lobbyShell.toString();
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }
}