package InterfaceTests;

import controllers.LobbyController;
import interfacesUI.ILobby;
import models.Lobby;
import models.LobbyShell;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ILobbyTest {

    ILobby LC;
    boolean exceptionThrown;
    LobbyShell publicLobbyShell;
    Lobby publicLobby;
    Player player;

    @BeforeEach
    public void setUp(){
        LC = new LobbyController();
        exceptionThrown = false;
        ((LobbyController)LC).setShowAlerts(false);
        publicLobbyShell = new LobbyShell(4, "newLobbyShell", false, 3);
        publicLobby = new Lobby(publicLobbyShell);
        player = new Player("testPlayer");
    }

    @Test
    public void testAddPlayer(){
        int playersBefore = publicLobby.getPlayers().size();
        try {
            publicLobby.addPlayer(player);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertEquals(playersBefore + 1, publicLobby.getPlayers().size());
    }

    @Test
    public void testAddPlayerLobbyFull(){
        LobbyShell lobbyShellFull = new LobbyShell(1, "newLobbyShell", false, 3);
        Lobby lobbyFull = new Lobby(lobbyShellFull);
        lobbyFull.addPlayer(player);
        boolean result = lobbyFull.addPlayer(new Player("lol"));
        assertFalse(result);
        assertEquals(0, publicLobby.getPlayers().size());
    }

    @Test
    public void testRemovePlayerHappyFlow(){
        int playersBefore = publicLobby.getPlayers().size();
        publicLobby.addPlayer(player);
        int playersAfter = publicLobby.getPlayers().size();
        try{
            publicLobby.removePlayer(player);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertEquals(playersBefore, playersAfter - 1);
    }

    @Test
    public void testRemovePlayerInvalidPlayer(){
        int playersBefore = publicLobby.getPlayers().size();
        boolean result = publicLobby.removePlayer(player);
        assertFalse(result);
        assertEquals(playersBefore, publicLobby.getPlayers().size());
    }

    //Methods will maybe not be implemented

    /*@Test
    public void testRemovePlayerByPosHappyFlow(){
        publicLobby.addPlayer(player);
        try{
            publicLobby.removePlayer(0);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void testRemovePlayerByPosIncorrectPos(){
        publicLobby.addPlayer(player);
        try{
            publicLobby.removePlayer(5);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void testRemovePlayerByName(){
        publicLobby.addPlayer(player);
        try{
            publicLobby.removePlayer(player.getUsername());
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void testSetLobbyName(){

    }*/

    @Test
    public void testSetRoomToken(){
        try {
            publicLobbyShell.setToken("newToken");
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertEquals("newToken", publicLobbyShell.getToken());
    }
}
