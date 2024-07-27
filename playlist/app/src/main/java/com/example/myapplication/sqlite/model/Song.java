package com.example.myapplication.sqlite.model;

public class Song {
    long id;
    String name;
    String artist;
    String genre;

    // constructors
    public Song() {
    }

    public Song(String name, String artist, String genre) {
        this.name = name; this.artist=artist; this.genre = genre;
    }

    public Song(long id, String name, String artist, String genre) {
        this.id = id;
        this.name = name;
        this.artist=artist;
        this.genre = genre;
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

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}


