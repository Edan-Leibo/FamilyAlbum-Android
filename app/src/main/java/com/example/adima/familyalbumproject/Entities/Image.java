package com.example.adima.familyalbumproject.Entities;

/**
 * Created by adima on 03/03/2018.
 */

public class Image {
    private String imageUrl;
    private String albumId;
    private String name;

    public Image(){

    }

    public Image(String imageUrl, String albumId, String name) {
        this.imageUrl = imageUrl;
        this.albumId = albumId;
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
