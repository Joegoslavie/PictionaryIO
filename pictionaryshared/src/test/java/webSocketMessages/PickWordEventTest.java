package webSocketMessages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PickWordEventTest {
    boolean exceptionThrown;

    @BeforeEach
    public void setUp(){
        exceptionThrown = false;
    }

    @Test
    public void constructorTest(){
        PickWordEvent pickWordEvent = null;
        try{
            pickWordEvent = new PickWordEvent(new ArrayList<>(), "name");
            pickWordEvent = new PickWordEvent("name");
            pickWordEvent = new PickWordEvent();
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }
}