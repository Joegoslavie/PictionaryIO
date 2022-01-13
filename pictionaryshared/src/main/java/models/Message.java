package models;

import enums.MessageType;

import java.util.Date;

public class Message {

    private Date time;
    //TODO: change this..
    private int round;
    private String message;
    private String username;
    private MessageType msgType;

    public Message(Date time, String message, String username, MessageType msgType) {
        this.time = time;
        this.round = 0;
        this.message = message;
        this.username = username;
        this.msgType = msgType;
    }

    public Message(Date time, int round, String message, String username, MessageType msgType) {
        this.time = time;
        this.round = round;
        this.message = message;
        this.username = username;
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return this.getUsername() + ": " + this.getMessage();
    }

    public Date getTime() {
        return time;
    }

    public int getRound() {
        return round;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public MessageType getMsgType() {
        return msgType;
    }
}
