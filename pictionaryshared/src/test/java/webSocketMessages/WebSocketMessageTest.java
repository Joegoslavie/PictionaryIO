package webSocketMessages;

import enums.Header;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class WebSocketMessageTest {
    WebSocketMessage webSocketMessage;
    boolean exceptionThrown;

    @BeforeEach
    public void setUp(){
        webSocketMessage = new WebSocketMessage(Header.LOGIN, "jsonString");
        exceptionThrown = false;
    }

    @Test
    public void constructorTest(){
        WebSocketMessage webSocketMessage = null;
        try{
            webSocketMessage = new WebSocketMessage();
            webSocketMessage = new WebSocketMessage(Header.LOGIN, "jsonString");
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void gettersTest(){
        Header header = null;
        String content = null;
        try{
            header = webSocketMessage.getHeader();
            content = webSocketMessage.getContent();
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertEquals(header, Header.LOGIN);
        assertEquals(content, "jsonString");
    }
}