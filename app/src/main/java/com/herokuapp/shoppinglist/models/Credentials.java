package com.herokuapp.shoppinglist.models;

public class Credentials {
    String id, password;

    public Credentials(String id, String password){
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}