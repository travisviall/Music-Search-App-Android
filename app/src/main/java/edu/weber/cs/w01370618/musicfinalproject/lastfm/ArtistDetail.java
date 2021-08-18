package edu.weber.cs.w01370618.musicfinalproject.lastfm;

import java.util.List;

public class ArtistDetail {

    protected String name;
    protected List<Tag> tags;
    protected List<Bio> biography;

    public String getName() {
        return name;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public List<Bio> getBiography() {
        return biography;
    }
}
