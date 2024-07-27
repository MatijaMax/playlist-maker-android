package com.example.myapplication.sqlite.model;

public class Playlist {

    long id;
    String name;
    String username;

    // constructors
    public Playlist() {
    }

    public Playlist(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public Playlist(long id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
