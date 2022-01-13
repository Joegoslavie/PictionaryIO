package mocks;

import restmodels.RESTPlayer;
import serverInterfaces.IRestClient;

import java.net.HttpURLConnection;
import java.util.HashMap;

public class MockRestClient implements IRestClient {
    @Override
    public RESTPlayer register(String username, String password) {
        return null;
    }

    @Override
    public RESTPlayer signIn(String username, String password) {
        return null;
    }

    @Override
    public boolean signOut(String username) {
        return false;
    }

    @Override
    public boolean executeGet(String username) {
        return false;
    }

    @Override
    public RESTPlayer executePost(HashMap<String, String> accountParams, String operation) {
        return null;
    }

    @Override
    public RESTPlayer parseResponse(HttpURLConnection connection) {
        return null;
    }

    @Override
    public String createPostDataString(HashMap<String, String> payload) {
        return null;
    }
}
