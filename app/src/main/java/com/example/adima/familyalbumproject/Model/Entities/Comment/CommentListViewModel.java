package com.example.adima.familyalbumproject.Model.Entities.Comment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.adima.familyalbumproject.Model.Repositories.CommentRepository;

import java.util.List;

/**
 * Created by adima on 04/03/2018.
 */
/*
represents the view model of comments list
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




