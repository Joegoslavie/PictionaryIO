package serverInterfaces;

import restmodels.RESTPlayer;

import java.net.HttpURLConnection;
import java.util.HashMap;

public interface IRestClient {
    RESTPlayer register(String username, String password);
    RESTPlayer signIn(String username, String password);
    boolean signOut(String username);
    boolean executeGet(String username);
    RESTPlayer executePost(HashMap<String, String> accountParams, String operation);
    RESTPlayer parseResponse(HttpURLConnection connection);
    String createPostDataString(HashMap<String, String> payload);
}
