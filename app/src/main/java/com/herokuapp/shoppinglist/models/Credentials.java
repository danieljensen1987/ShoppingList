package com.herokuapp.shoppinglist.models;

public class Credentials {
    String email, password;

    public Credentials(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}