package InterfaceTests;

import controllers.LoginController;
import interfacesUI.ILogin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ILoginTest {
    ILogin LC;
    boolean exceptionThrown;

    @BeforeEach
    public void setUp(){
        LC = new LoginController();
        exceptionThrown = false;
        ((LoginController)LC).setShowAlerts(false);
    }

    @Test
    public void testLoginHappyFlow(){
        String username = "daanUsername";
        String password = "admin";
        try {
            LC.login(username, password);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testLoginNameTooShort(){
        String username = "s";
        String password = "admin";
        try {
            LC.login(username, password);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testLoginPasswordTooShort(){
        String username = "daanUsername";
        String password = "d";
        try {
            LC.login(username, password);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testLoginNameToolong(){
        String username = "daanUsernamedaanUsernamedaanUsernamedaanUsernamedaanUsername";
        String password = "admin";
        try {
            LC.login(username, password);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testLoginPasswordToolong(){
        String username = "admin";
        String password = "adminadminadminadminadmin";
        try {
            LC.login(username, password);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
}
