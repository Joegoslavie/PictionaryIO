package models;

import enums.RoundState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class RoundTest {
    Round round;
    boolean exceptionThrown;

    @BeforeEach
    public void setUp(){
        round = new Round("name");
        round.setWordToDraw("koekje");
        exceptionThrown = false;
    }

    @Test
    public void constructorTest(){
        try{
            Round round = new Round("name");
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void gettersTest(){
        String drawerName = null;
        String wordToDraw = null;
        try{
            drawerName = round.getDrawerName();
            wordToDraw = round.getWordToDraw();
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertEquals(drawerName, "name");
        assertEquals(wordToDraw, "koekje");
    }

    @Test
    public void setWordToDrawTest(){
        String wordToDraw = "lolol";
        try{
            round.setWordToDraw(wordToDraw);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertEquals(round.getWordToDraw(), "lolol");
    }

    @Test
    public void setRoundStateTest(){
        RoundState roundState = RoundState.STARTED;
        try{
            round.setRoundState(roundState);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertEquals(round.getRoundState(), RoundState.STARTED);
    }
}