package com.example.myapplication.sqlite.model;

public class Artist {
    long id;
    String name;

    String genre;

    // constructors
    public Artist() {
    }

    public Artist(String name, String genre) {
        this.name = name; this.genre = genre;
    }

    public Artist(long id, String name, String genre) {
        this.id = id;
        this.name = name;
        this.genre = genre;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
}

