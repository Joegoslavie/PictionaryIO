package webSocketMessages;

import enums.Header;

public class WebSocketMessage {
    private Header header;
    private String content;

    public Header getHeader(){
        return header;
    }
    public String getContent(){
        return content;
    }

    public WebSocketMessage(){}
    public WebSocketMessage(Header header, String content){
        this.header = header;
        this.content = content;
    }
}
