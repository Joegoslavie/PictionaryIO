package api.controllers;

import api.interfaces.IAccountController;
import mocks.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountControllerTest {

    DatabaseConnectionMock databaseConnectionMock;
    AccountController accountControllerMock;
    PrepareStatementMock prepareStatementMock;
    ResultSetMock resultSetMock;
    boolean exceptionThrown;

    @BeforeEach
    public void setUp(){
        databaseConnectionMock = new DatabaseConnectionMock();
        accountControllerMock = new AccountController();
        prepareStatementMock = new PrepareStatementMock();
        resultSetMock = new ResultSetMock();
        accountControllerMock.getPlayerRepository().setConnection(databaseConnectionMock);
        exceptionThrown = false;
    }

    @Test
    public void registerTest(){
        String username = "namenamename";
        String password = "pwp2232wpw";
        try{
            accountControllerMock.register(username, password);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void loginTest(){
        String username = "namenamename";
        String password = "pwp2232wpw";
        try{
            accountControllerMock.login(username, password);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void signOutTest(){
        String username = "namenamename";
        try{
            accountControllerMock.signOut(username);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }
}