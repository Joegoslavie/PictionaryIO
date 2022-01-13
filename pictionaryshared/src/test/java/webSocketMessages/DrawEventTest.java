package webSocketMessages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DrawEventTest {
    boolean exceptionThrown;

    @BeforeEach
    public void setUp(){
        exceptionThrown = false;
    }

    @Test
    public void constructorTest(){
        DrawEvent drawEvent = null;
        try{
            drawEvent = new DrawEvent();
            drawEvent = new DrawEvent(1, 1, 0, 0);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }
}