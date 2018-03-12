package com.example.adima.familyalbumproject.Model.Entities.Comment;

import android.util.Log;

import com.example.adima.familyalbumproject.Model.Firebase.ModelFirebase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by adima on 04/03/2018.
 */

public class CommentFirebase {
    private static ChildEventListener listener;
    private static DatabaseReference lastRef;

    CommentFirebase() {
    }


    public interface Callback<T> {
        void onComplete(T data);
    }

    public interface CallbackOnCommentUpdate<Comment> {
        void onAdded(Comment data);
        void onDeleted(Comment data);
        void initialData(List<Comment> commentList);
    }

    public static void observeAllComments(String albumId, long lastUpdate, final CallbackOnCommentUpdate<Comment> callback) {
        Log.d("TAG", "getAllCommentsAndObserve " + lastUpdate);
        Log.d("TAG", "getAllCommentsAndObserve");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (listener!=null && lastRef!=null) {
            lastRef.removeEventListener(listener);
        }

        DatabaseReference myRef = database.getReference("comments").child(albumId);
        lastRef=myRef;

        Query query = myRef.orderByChild("lastUpdated").startAt(lastUpdate);
        Log.d("TAG", "the query is ok");

        listener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                callback.onAdded(dataSnapshot.getValue(Comment.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                callback.onDeleted(dataSnapshot.getValue(Comment.class));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        myRef.addChildEventListener(listener);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Comment> list = new LinkedList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Comment comment = snap.getValue(Comment.class);
                    list.add(comment);
                }
                callback.initialData(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.initialData(null);
            }
        });
    }

    public interface OnCreationComment {
        public void onCompletion(boolean success);
    }

    public static void addComment(String albumId, Comment comment, final OnCreationComment listener) {
        Log.d("TAG", "add comment to firebase");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = database.getReference("comments").child(albumId).push().getKey();
        comment.setCommentId(key);

        HashMap<String, Object> json = comment.toJson();
        json.put("lastUpdated", ServerValue.TIMESTAMP);

        Log.d("TAG", "the command id is:" + comment.getCommentId());
        DatabaseReference ref = database.getReference("comments").child(albumId).child(comment.getCommentId());
        ref.setValue(json, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("TAG", "Error: Comment could not be saved "
                            + databaseError.getMessage());
                    listener.onCompletion(false);
                } else {
                    Log.e("TAG", "Success : Comment saved successfully.");
                    listener.onCompletion(true);
                }
            }
        });

    }

    public static void removeComment(Comment comment, final ModelFirebase.OnRemove listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("comments").child(comment.getAlbumId()).child(comment.getCommentId());
        ref.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    listener.onCompletion(false);
                } else {
                    listener.onCompletion(true);
                }
            }
        });
    }

}
