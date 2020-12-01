package com.example.infosys1d_amigoproject.models;

public class Chat {
    private String Sender;
    private String Receiver;
    private String message;

    public Chat(String sender, String receiver, String message) {
        this.Sender = sender;
        this.Receiver = receiver;
        this.message = message;
    }

    public Chat(Chat chat) {
        this.Sender = chat.getSender();
        this.Receiver = chat.getReceiver();
        this.message = message;
    }

    public Chat() {
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String receiver) {
        Receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
