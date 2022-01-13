package InterfaceTests;

import controllers.LobbyOverviewController;
import interfacesUI.ILobbyOverview;
import models.Lobby;
import models.LobbyShell;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class ILobbyOverviewTest{
    ILobbyOverview LOC;
    boolean exceptionThrown;
    LobbyShell publicLobbyShell;
    Lobby publicLobby;
    LobbyShell privateLobbyShell;
    Lobby privateLobby;
    Player player;

    @BeforeEach
    public void setUp(){
        LOC = new LobbyOverviewController();
        exceptionThrown = false;
        ((LobbyOverviewController)LOC).setShowAlerts(false);
        publicLobbyShell = new LobbyShell(4, "newLobbyShell", false, 3);
        publicLobby = new Lobby(publicLobbyShell);
        privateLobbyShell = new LobbyShell(4, "newLobbyShell", true, 3);
        privateLobby = new Lobby(privateLobbyShell);
        player = new Player("testPlayer");
    }

    @Test
    public void testSearchLobby() {
        ArrayList<Lobby> lobbies = new ArrayList<>();
        lobbies.add(publicLobby);
        lobbies.add(privateLobby);

        String searchInput = "Lobby";
        try{
            LOC.searchLobby(searchInput);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void testJoinPublicLobby(){
        try {
            publicLobby.join(player);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void testJoinPrivateLobby(){
        String token = privateLobbyShell.getToken();
        try {
            privateLobby.join(player, token);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }
}
