package com.nsnsolutions.bchat;


import android.text.Editable;

public class Message {
    private String message;
    //private String time;
    private String user;
    private String mac;

    //constructors
    public Message(String message, String user,String mac) {
        this.message = message;
        //this.time = time;
        this.user = user;
        this.mac = mac.replaceAll("[^a-zA-Z0-9]", "");;
    }

    public Message(Editable text, String format, String generatedString1, String generatedString2) {
    }




    //toString
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

    /*public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }*/

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
