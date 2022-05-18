package com.nsnsolutions.bchat;

import android.text.Editable;

/**
 * Message is a class to use to make message object
 */

public class Message {

    private String message; //string use to store the content of the message
    private String user;    //variable use to keep track whether message was sent(S) or Received(R)
    private String mac;     //variable use to keep the mac address of the receiver

    //constructors
    public Message(String message, String user,String mac) {
        this.message = message;
        this.user = user;
        this.mac = mac.replaceAll("[^a-zA-Z0-9]", "");;
    }

    public Message(Editable text, String format, String generatedString1, String generatedString2) {
    }




    //toString Method
    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", user=" + user +
                ", mac=" + mac +
                '}';
    }

    //getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
