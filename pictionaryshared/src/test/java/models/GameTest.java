package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GameTest {
    List<Player> players;
    Game game;
    Lobby lobby;
    LobbyShell lobbyShell;
    boolean exceptionThrown;

    @BeforeEach
    public void setUp(){
        lobbyShell = new LobbyShell(2, "lobbyShell", false, 3);
        lobby = new Lobby(lobbyShell);
        players = new ArrayList<>();
        Player player1 = new Player("name1");
        Player player2 = new Player("name2");
        players.add(player1);
        players.add(player2);

        for(Player p : players){
            lobby.addPlayer(p);
        }

        game = new Game("newGame", lobby);
        exceptionThrown = false;
    }

    //constructors (and getters/setters)
    @Test
    public void constructorTest(){
        Game game = null;
        try{
            LobbyShell lobbyShell = new LobbyShell(2, "lobbyShell", false, 3);
            Lobby lobby = new Lobby(lobbyShell);
            lobby.addPlayer(new Player("name1"));
            lobby.addPlayer(new Player("name2"));
            game = new Game("newGame2", lobby);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void gettersTest(){
        List<Player> players = null;
        List<LeaderboardEntry> leaderboardEntries = null;
        List<Round> rounds = null;
        int amountOfTurnsPerPlayer = -1;

        try{
            players = game.getPlayers();
            leaderboardEntries = game.getLeaderboardEntries();
            rounds = game.getRounds();
            amountOfTurnsPerPlayer = game.getAmountOfTurnsPerPlayer();
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertEquals(players.size(), 2);
        assertEquals(leaderboardEntries.size(), 2);
        assertEquals(rounds.size(), 1);
        assertEquals(amountOfTurnsPerPlayer, 3);
    }

    //model logic
    @Test
    public void startNewRoundTest(){
        int roundsBefore = game.getRounds().size();
        int roundsAfter = 0;
        try{
            game.startNewRound();
            roundsAfter = game.getRounds().size();
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertEquals(roundsAfter, roundsBefore + 1);
    }
}