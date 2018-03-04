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

    public CommentListViewModel() {
        comments = CommentRepository.instance.getAllComments();
    }

    public LiveData<List<Comment>> getCommentsList() {
        return comments;
    }




}




