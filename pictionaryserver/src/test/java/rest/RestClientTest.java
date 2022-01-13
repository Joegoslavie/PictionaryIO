package rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RestClientTest {
    RestClient restClient;
    boolean exceptionThrown;

    @BeforeEach
    public void setUp(){
        restClient = RestClient.getInstance();
        exceptionThrown = false;
    }

    //check if not throwing errors
    @Test
    public void registerTest(){
        String username = "username";
        String password = "password";
        try{
            restClient.register(username, password);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void loginTest(){
        String username = "username";
        String password = "password";
        try{
            restClient.signIn(username, password);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }
}