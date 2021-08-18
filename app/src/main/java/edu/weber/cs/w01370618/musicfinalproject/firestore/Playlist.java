package edu.weber.cs.w01370618.musicfinalproject.firestore;

public class Playlist {


    private String artist;
    private String song;
    private String duration;

    public Playlist() {

    }

    public Playlist(String artist, String song) {
        this.artist = artist;
        this.song = song;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
