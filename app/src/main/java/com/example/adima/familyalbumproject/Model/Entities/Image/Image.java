package com.example.adima.familyalbumproject.Model.Entities.Image;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adima on 03/03/2018.
 */
/*
this class represents an image in an album
 */
@Entity
public class Image {
    @PrimaryKey
    @NonNull
    private String imageId;
    private String imageUrl;
    private String albumId;
    private String name;
    private long lastUpdated;
    public Image(){

    }
    public Image(String imageUrl, String albumId, String name) {
        this.imageUrl = imageUrl;
        this.albumId = albumId;
        this.name = name;
    }


    @NonNull
    public String getImageId() {
        return imageId;
    }
    public void setImageId(@NonNull String imageId) {
        this.imageId = imageId;
    }
    public long getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
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

    public Image (Map<String,Object> ImgagFromFirebase){
        this.imageUrl=(String)ImgagFromFirebase.get("imageUrl");
        this.imageId=(String)ImgagFromFirebase.get("imageId");
        this.albumId=(String)ImgagFromFirebase.get("albumId");
        this.name=(String)ImgagFromFirebase.get("name");
        this.lastUpdated=(long)ImgagFromFirebase.get("lastUpdated");

    }

    public HashMap<String,Object> toJson(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("albumId",albumId);
        result.put("name",name);
        result.put("imageUrl",imageUrl);
        result.put("imageId",imageId);
        result.put("lastUpdated",lastUpdated);

        return result;
    }



}
