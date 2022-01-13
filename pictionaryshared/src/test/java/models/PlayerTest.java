package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.websocket.Session;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    Player player;
    boolean exceptionThrown;

    @BeforeEach
    public void setUp(){
        player = new Player("name");
        exceptionThrown = false;
    }

    @Test
    public void constructorTest(){
        Player player = null;
        try{
            player = new Player("name1");
            player = new Player();
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void gettersTest(){
        int turnCount = -1;
        String username = null;
        Session session = null;

        try{
            player.incrementTurnCount();
            turnCount = player.getTurnCount();
            username = player.getUsername();
            session = player.getSession();
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertEquals(turnCount, 1);
        assertEquals(username, "name");
        assertNull(session);
    }

    @Test
    public void setGuessedWordTest(){
        boolean guessedWord = false;
        try{
            player.setGuessedWord(true);
            guessedWord = player.hasGuessedWord();
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertTrue(guessedWord);
    }
}