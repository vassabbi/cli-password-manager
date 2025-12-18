package com.example.passwordManager.Model;

public class UserSession {
    private final String username;
    private final char[] password;

    public UserSession(String username, char[] password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public char[] getPassword() {
        return password;
    }
}
