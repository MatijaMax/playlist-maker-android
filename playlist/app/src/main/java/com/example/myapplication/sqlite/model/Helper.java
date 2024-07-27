package com.example.myapplication.sqlite.model;

public class Helper {

    long id;
    long playlistId;

    long songId;

    String displayName;

    public Helper() {
    }

    public Helper(long playlistId, long songId, String displayName) {
        this.playlistId = playlistId;
        this.songId = songId;
        this.displayName = displayName;
    }

    public Helper(long id, long playlistId, long songId, String displayName) {
        this.id = id;
        this.playlistId = playlistId;
        this.songId = songId;
        this.displayName = displayName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(long playlistId) {
        this.playlistId = playlistId;
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
