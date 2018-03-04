package com.example.adima.familyalbumproject.Comment.Model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.adima.familyalbumproject.Album.Model.CommentRepository;

import java.util.List;

/**
 * Created by adima on 04/03/2018.
 */

public class CommentListViewModel extends ViewModel {
    private LiveData<List<Comment>> comments;

    public void init(String albumID){
        comments = CommentRepository.instance.getAllComments(albumID);
    }

    public CommentListViewModel() {}

    public LiveData<List<Comment>> getCommentsList() {
        return comments;
    }




}




