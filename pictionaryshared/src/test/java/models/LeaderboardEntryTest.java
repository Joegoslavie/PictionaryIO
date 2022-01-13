package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class LeaderboardEntryTest {
    LeaderboardEntry leaderboardEntry;
    boolean exceptionThrown;

    @BeforeEach
    public void setUp(){
        leaderboardEntry = new LeaderboardEntry("name", 100);
        exceptionThrown = false;
    }

    //constructors (and getters/setters)
    @Test
    public void constructorTest(){
        LeaderboardEntry leaderboardEntry = null;
        try{
            leaderboardEntry = new LeaderboardEntry("name", 100);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void gettersTest(){
        String username = null;
        int score = -1;
        try{
            username = leaderboardEntry.getUsername();
            score = leaderboardEntry.getScore();
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertEquals(username, "name");
        assertEquals(score, 100);
    }
}