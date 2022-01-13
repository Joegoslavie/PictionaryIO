package restmodels;

public class RESTPlayer {

    private int id;
    private String username;
    private String password;

    public RESTPlayer(int id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public RESTPlayer(){

    }

    public int getId(){
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
