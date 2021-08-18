package edu.weber.cs.w01370618.musicfinalproject.lastfm;

public class Artist {

    protected String name;
    protected String mbid;
    protected String genre;
    private String url;

    public String getName() {
        return name;
    }

    public String getMbid() {
        return mbid;
    }

    public String getGenre() {
        return genre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}

