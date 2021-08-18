package edu.weber.cs.w01370618.musicfinalproject.lastfm;

import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("#text")
    protected String imageURL;

    protected String size;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
