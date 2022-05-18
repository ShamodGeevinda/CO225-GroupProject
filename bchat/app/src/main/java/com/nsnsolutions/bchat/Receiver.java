package com.nsnsolutions.bchat;


/**
 *This is a class created to make a receiver object
 */

public class Receiver {

    //defining attributes
    private String mac;
    private String name;

    //constructor
    public Receiver(String mac, String name) {
        this.mac = mac;
        this.name = name;
    }

    public Receiver() {
    }

    //toString method
    @Override
    public String toString() {
        return "Receiver{" +
                "mac=" + mac.toString() +
                ", name='" + name.toString() + '\'' +
                '}';
    }

    //getters and setters
    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
