package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class LobbySettingsTest {
    LobbySettings lobbySettings;
    boolean exceptionThrown;

    @BeforeEach
    public void setUp(){
        lobbySettings = new LobbySettings(false, "name", 2);
        exceptionThrown = false;
    }

    //constructors (and getters/setters)
    @Test
    public void constructorTest(){
        LobbySettings lobbySettings = null;
        try{
            lobbySettings = new LobbySettings(false, "name", 2);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }
}