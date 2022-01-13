package restmodels;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class RESTPlayerTest {
    RESTPlayer restPlayer;
    boolean exceptionThrown;

    @BeforeEach
    public void setUp(){
        restPlayer = new RESTPlayer(1, "name", "password");
        exceptionThrown = false;
    }

    //constructors (and getters/setters)
    @Test
    public void constructorTest() {
        try {
            RESTPlayer restPlayer = new RESTPlayer(1, "name", "password");
            RESTPlayer restPlayer1 = new RESTPlayer();
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void gettersTest(){
        int id = -1;
        String username = null;
        String password = null;
        try{
            id = restPlayer.getId();
            username = restPlayer.getUsername();
            password = restPlayer.getPassword();
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertEquals(id, 1);
        assertEquals(username, "name");
        assertEquals(password, "password");
    }
}