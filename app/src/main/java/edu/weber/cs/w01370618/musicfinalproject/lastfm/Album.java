package edu.weber.cs.w01370618.musicfinalproject.lastfm;

import java.util.List;

public class Album {

    protected String name;
    protected List<Image> image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Image> getImages() {
        return image;
    }

    public void setImages(List<Image> images) {
        this.image = images;
    }
}
