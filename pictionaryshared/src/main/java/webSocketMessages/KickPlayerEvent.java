package webSocketMessages;

public class KickPlayerEvent {
    public String username;

    public KickPlayerEvent(){}
    public KickPlayerEvent(String username){
        this.username = username;
    }
}
