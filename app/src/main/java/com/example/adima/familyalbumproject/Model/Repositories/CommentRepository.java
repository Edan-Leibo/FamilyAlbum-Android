package com.example.adima.familyalbumproject.Model.Repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.adima.familyalbumproject.Model.Entities.Comment.Comment;
import com.example.adima.familyalbumproject.Model.Entities.Comment.CommentFirebase;
import com.example.adima.familyalbumproject.Controller.Start.MyApplication;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by adima on 04/03/2018.
 */
/*
This class represnts Comment repository
 */
public class CommentRepository {
    public static final CommentRepository instance = new CommentRepository();

    CommentRepository() {
    }

    MutableLiveData<List<Comment>> commentsListliveData;


    public LiveData<List<Comment>> getAllComments(final String albumId) {
        synchronized (this) {
           // if (commentsListliveData == null) {
                Log.d("TAG","Live data is null");
                commentsListliveData = new MutableLiveData<List<Comment>>();

                //1. get the last update date
                long lastUpdateDate = 0;
                try {
                    lastUpdateDate = MyApplication.getMyContext()
                            .getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("lastUpdateDateComments"+albumId, 0);
                }catch (Exception e){

                }


                CommentFirebase.observeAllComments(albumId, lastUpdateDate, new CommentFirebase.CallbackOnCommentUpdate<Comment>() {
                    @Override
                    public void onAdded(Comment data) {

                        List<Comment> list = new LinkedList<>();
                        list.add(data);
                        addCommentDataInLocalStorage(list,albumId);
                    }

                    @Override
                    public void onDeleted(Comment data) {
                        List<Comment> list = new LinkedList<>();
                        list.add(data);
                        deleteCommentDataInLocalStorage(list,albumId);
                    }

                    @Override
                    public void initialData(List<Comment> comments) {
                        addCommentDataInLocalStorage(comments, albumId);
                    }


                });
        }
        return commentsListliveData;
    }

/*
    private void getAllCommentDataInLocalStorage(String albumId) {
        GetAllTask task = new GetAllTask();
        task.setAlbumId(albumId);
        task.execute();
    }
*/
    private void addCommentDataInLocalStorage(List<Comment> data, String albumId) {
        AddingTask task = new AddingTask();
        task.setAlbumId(albumId);
        task.execute(data);
    }

    private void deleteCommentDataInLocalStorage(List<Comment> data,String albumId) {
        DeletionTask task = new DeletionTask();
        task.setAlbumId(albumId);
        task.execute(data);
    }

    public void removeFromLocalDb(Comment comment) {
        List<Comment> list = new LinkedList<>();
        list.add(comment);
        deleteCommentDataInLocalStorage(list,comment.getAlbumId());
    }

    ///
    class GetAllTask extends AsyncTask<List<Comment>,String,List<Comment>> {

        private String albumId;

        public void setAlbumId(String albumId) {
            this.albumId = albumId;
        }

        @Override
        protected List<Comment> doInBackground(List<Comment>[] lists) {

            List<Comment> commentList = AppLocalStore.db.commentDao().loadAllByIds(albumId);
            Log.d("TAG","finish updateEmployeeDataInLocalStorage in thread");

            return commentList;
        }

        @Override
        protected void onPostExecute(List<Comment> comments) {
            super.onPostExecute(comments);
            commentsListliveData.setValue(comments);
        }
    }

    ////

    class DeletionTask extends AsyncTask<List<Comment>,String,List<Comment>> {

        private String albumId;

        public void setAlbumId(String albumId) {
            this.albumId = albumId;
        }

        @Override
        protected List<Comment> doInBackground(List<Comment>[] lists) {
            List<Comment> data = lists[0];

            for (Comment comment : data) {
                    AppLocalStore.db.commentDao().delete(comment);
            }

            List<Comment> commentList = AppLocalStore.db.commentDao().loadAllByIds(albumId);
            return commentList;
        }

        @Override
        protected void onPostExecute(List<Comment> comments) {
            super.onPostExecute(comments);
            commentsListliveData.setValue(comments);
        }
    }



/////
    class AddingTask extends AsyncTask<List<Comment>,String,List<Comment>> {
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
                            .getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("lastUpdateDateComments"+albumId, 0);

                    Log.d("Tag","got the last update date");
                }catch (Exception e){
                    Log.d("Tag","in the exception");


                }
                if (data != null && data.size() > 0) {
                    //3. update the local DB
                    long reacentUpdate = lastUpdateDate;

                    for (Comment comment : data) {
                        if (comment.getCommentId() != null) {

                            AppLocalStore.db.commentDao().insertAll(comment);
                            Log.d("Tag", "after insert all");

                            if (comment.getLastUpdated() > reacentUpdate) {
                                reacentUpdate = comment.getLastUpdated();
                            }
                            Log.d("TAG", "updating: " + comment.toString());
                        }
                    }
                    SharedPreferences.Editor editor = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
                    editor.putLong("lastUpdateDateComments"+albumId, reacentUpdate);
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
