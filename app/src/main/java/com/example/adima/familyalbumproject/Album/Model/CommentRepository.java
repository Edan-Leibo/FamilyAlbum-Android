package com.example.adima.familyalbumproject.Album.Model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.adima.familyalbumproject.Comment.Model.Comment;
import com.example.adima.familyalbumproject.Comment.Model.CommentFirebase;
import com.example.adima.familyalbumproject.MyApplication;

import java.util.List;

/**
 * Created by adima on 04/03/2018.
 */

public class CommentRepository {


    public static final CommentRepository instance = new CommentRepository();

    CommentRepository() {
    }
    MutableLiveData<List<Comment>> commentsListliveData;

    public LiveData<List<Comment>> getCommentsList(String albumId) {
        synchronized (this) {
            if (commentsListliveData == null) {
                Log.d("TAG", "comment live data is null");

                commentsListliveData = new MutableLiveData<List<Comment>>();

                CommentFirebase.getAllCommentsAndObserve(albumId,new CommentFirebase.Callback<List<Comment>>() {
                    @Override
                    public void onComplete(List<Comment> data) {

                            if (data != null) commentsListliveData.setValue(data);
                            Log.d("TAG", "got comments data");

                    }
                });


            }
        }
        return commentsListliveData;
    }

    public LiveData<List<Comment>> getAllComments(final String albumId) {
        synchronized (this) {
            if (commentsListliveData == null) {
                Log.d("TAG","Live data is null");
                commentsListliveData = new MutableLiveData<List<Comment>>();

                //1. get the last update date
                long lastUpdateDate = 0;
                try {
                    lastUpdateDate = MyApplication.getMyContext()
                            .getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("lastUpdateDate", 0);
                }catch (Exception e){

                }


                CommentFirebase.getAllCommentsAndObserve(albumId,lastUpdateDate, new CommentFirebase.Callback<List<Comment>>() {
                    @Override
                    public void onComplete(List<Comment> data) {

                        updateCommentDataInLocalStorage(data,albumId);
                    }
                });


            }
        }
        return commentsListliveData;
    }

    private void updateCommentDataInLocalStorage(List<Comment> data,String albumId) {
        Log.d("TAG", "got items from firebase: " + data.size());
        CommentRepository.MyTask task = new CommentRepository.MyTask();

        task.setAlbumId(albumId);

        task.execute(data);
    }

    class MyTask extends AsyncTask<List<Comment>,String,List<Comment>> {
        private String albumId;

        public void setAlbumId(String albumId) {
            this.albumId = albumId;
        }
        @Override
        protected List<Comment> doInBackground(List<Comment>[] lists) {
            Log.d("TAG","starting updateAlbumDataInLocalStorage in thread");
            if (lists.length > 0) {
                List<Comment> data = lists[0];
                long lastUpdateDate = 0;
                try {
                    lastUpdateDate = MyApplication.getMyContext()
                            .getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("lastUpdateDate", 0);

                    Log.d("Tag","got the last update date");
                }catch (Exception e){
                    Log.d("Tag","in the exception");


                }
                if (data != null && data.size() > 0) {
                    //3. update the local DB
                    long reacentUpdate = lastUpdateDate;

                    for (Comment comment : data) {


                        AppLocalStore.db.commentDao().insertAll(comment);
                        Log.d("Tag","after insert all");

                        if (comment.getLastUpdated() > reacentUpdate) {
                            reacentUpdate = comment.getLastUpdated();
                        }
                        Log.d("TAG", "updating: " + comment.toString());
                    }
                    SharedPreferences.Editor editor = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
                    editor.putLong("lastUpdateDate", reacentUpdate);
                    editor.commit();
                }
                //return the complete student list to the caller
                List<Comment> commentList = AppLocalStore.db.commentDao().loadAllByIds(albumId);
                Log.d("TAG","finish updateEmployeeDataInLocalStorage in thread");

                return commentList;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Comment> comments) {
            super.onPostExecute(comments);
            commentsListliveData.setValue(comments);
            Log.d("TAG","update updateAlbumDataInLocalStorage in main thread");
            Log.d("TAG", "got items from local db: " + comments.size());

        }
    }




}
