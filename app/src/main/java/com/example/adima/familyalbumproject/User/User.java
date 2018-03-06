package com.example.adima.familyalbumproject.User;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adima on 06/03/2018.
 */


@Entity
public class User {

    @PrimaryKey
    @NonNull
    private String emailUser;
    private String imageUrl;


    public User() {

    }

    public User(@NonNull String emailUser, String imageUrl) {
        this.emailUser = emailUser;
        this.imageUrl = imageUrl;
    }

    @NonNull
    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(@NonNull String emailUser) {
        this.emailUser = emailUser;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User(Map<String, Object> userFromFirebase) {

        this.imageUrl = (String) userFromFirebase.get("imageUrl");
        this.emailUser = (String) userFromFirebase.get("emailUser");


    }

    public HashMap<String, Object> toJson() {

        HashMap<String, Object> result = new HashMap<>();

        result.put("imageUrl", imageUrl);
       // result.put("emailUser", emailUser);


        return result;
    }
}