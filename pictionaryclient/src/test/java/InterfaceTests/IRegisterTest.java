package InterfaceTests;

import controllers.RegisterController;
import interfacesUI.IRegister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IRegisterTest {
    IRegister RC;
    boolean exceptionThrown;

    @BeforeEach
    public void setUp(){
        RC = new RegisterController();
        ((RegisterController) RC).setShowAlerts(false);
        exceptionThrown = false;
        ((RegisterController)RC).setShowAlerts(false);
    }

    @Test
    public void testRegisterHappyFlow(){
        String username = "unittest";
        String password = "testen";
        String passwordVerification = "testen";
        try {
            RC.register(username, password, passwordVerification);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testRegisterNameTooShort(){
        String username = "s";
        String password = "admin";
        String passwordVerification = "admin";
        try {
            RC.register(username, password, passwordVerification);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testRegisterPasswordTooShort(){
        String username = "admin";
        String password = "d";
        String passwordVerification = "d";
        try {
            RC.register(username, password, passwordVerification);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testRegisterNameToolong(){
        String username = "adminadminadminadminadmin";
        String password = "admin";
        String passwordVerification = "admin";
        try {
            RC.register(username, password, passwordVerification);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testRegisterPasswordToolong(){
        String username = "admin";
        String password = "adminadminadminadminadmin";
        String passwordVerification = "adminadminadminadminadmin";
        try {
            RC.register(username, password, passwordVerification);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testRegisterPasswordVerificationWrong1(){
        String username = "admin";
        String password = "adminadminadminadminadmin";
        String passwordVerification = "admin";
        try {
            RC.register(username, password, passwordVerification);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testRegisterPasswordVerificationWrong2(){
        String username = "admin";
        String password = "admin";
        String passwordVerification = "adminadminadminadminadmin";
        try {
            RC.register(username, password, passwordVerification);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
}