package com.example.passwordManager.Model;

public class Entry {
    private int id;
    private String serviceName;
    private String username;
    private String password;
    private String notes;
    private String url;

    public Entry(int id, String serviceName, String username, String password, String notes, String url) {
        this.id = id;
        this.serviceName = serviceName;
        this.username = username;
        this.password = password;
        this.notes = notes;
        this.url = url;
    }
    
    public int getId() {
        return id;
    }
    public String getServiceName() {
        return serviceName;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getNotes() {
        return notes;
    }
    public String getUrl() {
        return url;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    
}
