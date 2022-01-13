package webSocketMessages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.AccessControlContext;

import static org.junit.jupiter.api.Assertions.*;

public class AccountDataEventTest {
    boolean exceptionThrown;

    @BeforeEach
    public void setUp(){
        exceptionThrown = false;
    }

    @Test
    public void constructorTest(){
        AccountDataEvent accountDataEvent = null;
        try{
            accountDataEvent = new AccountDataEvent("username", "password");
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }
}