package com.example.adima.familyalbumproject.Comment.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adima on 03/03/2018.
 */

@Entity
public class Comment {
    @PrimaryKey
    @NonNull
    private String commentId;


    private String userId;
    private String albumId;
    private String text;
    private String imageUrl;
    public long lastUpdated;

    public Comment(String text,String userId,String albumId) {

        this.text = text;
        this.albumId=albumId;
        this.userId=userId;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @NonNull
    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(@NonNull String commentId) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Comment (Map<String,Object> commentFromFirebase){
        this.setCommentId((String)commentFromFirebase.get("commentId"));
        this.setAlbumId((String)commentFromFirebase.get("albumId"));
        this.setImageUrl((String)commentFromFirebase.get("imageUrl"));
        this.setLastUpdated((long)commentFromFirebase.get("lastUpdated"));
        this.setText((String)commentFromFirebase.get("text"));
        this.setUserId((String)commentFromFirebase.get("userId"));

    }

    public HashMap<String,Object> toJson(){

        HashMap<String,Object> result = new HashMap<>();
        result.put("commentId",this.commentId);
        result.put("albumId",this.albumId);
        result.put("imageUrl",this.imageUrl);
        result.put("lastUpdated",this.lastUpdated);
        result.put("text",this.text);
        result.put("userId",this.userId);

        return result;
    }

}