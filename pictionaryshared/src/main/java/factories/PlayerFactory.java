package factories;

import com.google.gson.Gson;
import restmodels.RESTPlayer;

public class PlayerFactory {

    private static Gson gson = new Gson();
    public static RESTPlayer parseRestPlayer(String jsonMessage){
        return gson.fromJson(jsonMessage, RESTPlayer.class);
    }

    public static models.Player parsePictionaryPlayer(){
        return null;
    }
}