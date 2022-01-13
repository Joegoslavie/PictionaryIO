package rest;

import factories.PlayerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restmodels.RESTPlayer;
import serverInterfaces.IRestClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class RestClient implements IRestClient {

    private String host = "http://localhost:8080";
    private static RestClient instance;

    private static final Logger log = LoggerFactory.getLogger(RestClient.class);


    private RestClient() {
    }

    public static RestClient getInstance() {
        if (instance == null) {
            instance = new RestClient();
        }

        return instance;
    }

    public RESTPlayer register(String username, String password) {
        HashMap<String, String> accountParams = new HashMap<>();
        accountParams.put("username", username);
        accountParams.put("password", password);
        return executePost(accountParams, "/account/register");
    }

    public RESTPlayer signIn(String username, String password) {
        HashMap<String, String> accountParams = new HashMap<>();
        accountParams.put("username", username);
        accountParams.put("password", password);
        return executePost(accountParams, "/account/login");
    }

    public boolean signOut(String username) {
        return executeGet(username);
    }

    // Currently build for just sign out, extend to global GET execution in the future
    // IF REQUIRED (prob not)
    public boolean executeGet(String username) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(host + "/account/logout?username=" + username);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("accept-encoding", "gzip, deflate");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:66.0) Gecko/20100101 Firefox/66.0");
            connection.setConnectTimeout(5000);
            connection.setDoOutput(true);

            if (connection.getResponseCode() == 200) {
                return Boolean.valueOf(connection.getResponseMessage());
            }

            // ahha...
            return false;

        } catch (Exception e) {
            log.error("Gaat helemaal fout hier: " + e.getMessage());
        }

        // pretty ugly
        return false;
    }

    public RESTPlayer executePost(HashMap<String, String> accountParams, String operation) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(host + operation);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("accept-encoding", "gzip, deflate");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:66.0) Gecko/20100101 Firefox/66.0");
            connection.setConnectTimeout(5000);
            connection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(this.createPostDataString(accountParams));
            wr.close();

            switch (connection.getResponseCode()) {
                case 200:
                    return parseResponse(connection);
                case 406:
                    return null;
                case 409:
                    return null;
            }

        } catch (Exception e) {
            log.error("Gaat helemaal fout hier: " + e.getMessage());
        }

        return null;
    }

    public RESTPlayer parseResponse(HttpURLConnection connection) {
        try {
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }

            rd.close();
            if (response.length() > 0) {
                return PlayerFactory.parseRestPlayer(response.toString());
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public String createPostDataString(HashMap<String, String> payload) {
        try {
            StringBuilder result = new StringBuilder();
            boolean first = true;

            for (HashMap.Entry<String, String> entry : payload.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
