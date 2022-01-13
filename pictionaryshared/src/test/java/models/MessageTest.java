package models;

import enums.MessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MessageTest {
    Message chatMessage;
    boolean exceptionThrown;

    @BeforeEach
    public void setUp(){
        chatMessage = new Message(new Date(), "hi", "username", MessageType.DEFAULT);
        exceptionThrown = false;
    }

    //constructors (and getters/setters)
    @Test
    public void constructorTest(){
        Message message = null;
        try{
            message = new Message(new Date(), "hi", "username", MessageType.DEFAULT);
            message = new Message(new Date(), 2, "hi", "username", MessageType.DEFAULT);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void gettersTest(){
        String message = null;
        String username = null;
        try{
            message = chatMessage.getMessage();
            username = chatMessage.getUsername();
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertEquals(message, "hi");
        assertEquals(username, "username");
    }
}