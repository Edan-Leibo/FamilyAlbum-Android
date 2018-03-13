package com.example.adima.familyalbumproject.Model.Entities.Comment;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;


/*
this class represents a comment of family member in an album
 */
@Entity
public class Comment {
    @PrimaryKey
    @NonNull
    private String commentId;
    private String userId;
    private String albumId;
    private String text;
    public long lastUpdated;

    public Comment(String text,String userId,String albumId) {
        this.text = text;
        this.albumId=albumId;
        this.userId=userId;
    }

    public Comment(){

    }
/*
get set methods
 */
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

    public Comment (Map<String,Object> commentFromFirebase){
        this.setCommentId((String)commentFromFirebase.get("commentId"));
        this.setAlbumId((String)commentFromFirebase.get("albumId"));
        this.setLastUpdated((long)commentFromFirebase.get("lastUpdated"));
        this.setText((String)commentFromFirebase.get("text"));
        this.setUserId((String)commentFromFirebase.get("userId"));
    }

    public HashMap<String,Object> toJson(){

        HashMap<String,Object> result = new HashMap<>();
        result.put("commentId",this.commentId);
        result.put("albumId",this.albumId);
        result.put("lastUpdated",this.lastUpdated);
        result.put("text",this.text);
        result.put("userId",this.userId);
        return result;
    }

}