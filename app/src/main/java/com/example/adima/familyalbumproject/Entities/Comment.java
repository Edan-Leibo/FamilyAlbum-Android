package com.example.adima.familyalbumproject.Entities;

/**
 * Created by adima on 03/03/2018.
 */

public class Comment {
    private String text;

    public Comment(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
