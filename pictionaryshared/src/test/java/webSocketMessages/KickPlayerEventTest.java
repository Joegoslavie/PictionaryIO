package webSocketMessages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KickPlayerEventTest {
    boolean exceptionThrown;

    @BeforeEach
    public void setUp(){
        exceptionThrown = false;
    }

    @Test
    public void constructorTest(){
        KickPlayerEvent kickPlayerEvent = null;
        try{
            kickPlayerEvent = new KickPlayerEvent("name");
            kickPlayerEvent = new KickPlayerEvent();
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }
}