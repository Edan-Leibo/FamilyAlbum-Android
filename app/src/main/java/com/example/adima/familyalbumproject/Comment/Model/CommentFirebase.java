package com.example.adima.familyalbumproject.Comment.Model;

import android.util.Log;

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
    public interface Callback<T>{
        void onComplete(T data);
    }

    public static void getAllCommentsAndObserve(final Callback<List<Comment>> callback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("comments");

        ValueEventListener listener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //List<Album> list = new LinkedList<Album>();

                    List<Comment> list = new LinkedList<Comment>();



                for(DataSnapshot snap:dataSnapshot.getChildren()){

                        Comment comment = snap.getValue(Comment.class);
                    Log.d("TAG",comment.getText());
                    list.add(comment);
                }

                callback.onComplete(list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG","error in db");

                callback.onComplete(null);

            }
        });
    }

    public static void getAllCommentsAndObserve(long lastUpdate,final Callback<List<Comment>> callback){
        Log.d("TAG", "getAllCommentsAndObserve " + lastUpdate);
        Log.d("TAG", "getAllCommentsAndObserve");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("comments");

        Query query = myRef.orderByChild("lastUpdated").startAt(lastUpdate);
        Log.d("TAG","the query is ok");

        ValueEventListener listener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TAG","the data changed");

                List<Comment> list = new LinkedList<Comment>();
                if(dataSnapshot ==null){
                    Log.d("TAG","the snapshot is null");

                }
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Log.d("TAG","got the children");

                    Comment comment = snap.getValue(Comment.class);
                    Log.d("TAG","got the data in comment repository"+comment.getAlbumId());
                    Log.d("TAG","got the data in comment repository"+comment.getCommentId());
                    Log.d("TAG","got the data in comment repository"+comment.getText());

                    Log.d("TAG","got the data in comment repository"+comment.getUserId());

                    list.add(comment);
                }
                callback.onComplete(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onComplete(null);
            }
        });
    }


    public static void addComment(Comment comment){
        Log.d("TAG", "add comment to firebase");


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String key = database.getReference("comments").push().getKey();


       comment.setCommentId(key);

        HashMap<String, Object> json = comment.toJson();
        json.put("lastUpdated", ServerValue.TIMESTAMP);

        //DatabaseReference myRef = database.getReference("albums");



        DatabaseReference ref = database.getReference("comments").child(comment.getCommentId());

        ref.setValue(json);
        //myRef.child(employee.id).setValue(json);
    }
}
