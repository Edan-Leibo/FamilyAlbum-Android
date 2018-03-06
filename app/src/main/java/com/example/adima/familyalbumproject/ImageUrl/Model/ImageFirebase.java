package com.example.adima.familyalbumproject.ImageUrl.Model;

import android.util.Log;

import com.example.adima.familyalbumproject.Entities.Image;
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
 * Created by adima on 05/03/2018.
 */

public class ImageFirebase {


    //public static  String albumId;

    ImageFirebase(){
        //this.albumId=albumId;

    }



    public interface Callback<T>{
        void onComplete(T data);
    }

    public static void getAllImagesAndObserve(String albumId,final ImageFirebase.Callback<List<Image>> callback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("images").child(albumId);


        ValueEventListener listener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //List<Album> list = new LinkedList<Album>();

                List<Image> list = new LinkedList<Image>();



                for(DataSnapshot snap:dataSnapshot.getChildren()){

                    Image image = snap.getValue(Image.class);
                    Log.d("TAG",image.getImageUrl());
                    list.add(image);
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

    public static void getAllImagesAndObserve(String albumId,long lastUpdate,final ImageFirebase.Callback<List<Image>> callback){
        Log.d("TAG", "getAllImagesAndObserve " + lastUpdate);
        Log.d("TAG", "getAllImagessAndObserve");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("comments");
        DatabaseReference myRef = database.getReference("images").child(albumId);

        Query query = myRef.orderByChild("lastUpdated").startAt(lastUpdate);
        Log.d("TAG","the query is ok");

        ValueEventListener listener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TAG","the data changed");

                List<Image> list = new LinkedList<Image>();
                if(dataSnapshot ==null){
                    Log.d("TAG","the snapshot is null");

                }
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Log.d("TAG","got the children");

                    Image image = snap.getValue(Image.class);


                    list.add(image);
                }
                callback.onComplete(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onComplete(null);
            }
        });
    }

    public interface OnCreationImage{
        public void onCompletion(boolean success);
    }

    public static void addImage(String albumId, Image image, final OnCreationImage listener){
        Log.d("TAG", "add image to firebase");


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String key = database.getReference("images").child(albumId).push().getKey();


        image.setImageId(key);

        HashMap<String, Object> json = image.toJson();
        json.put("lastUpdated", ServerValue.TIMESTAMP);


        //DatabaseReference myRef = database.getReference("albums");


       // Log.d("TAG","the command id is:"+comment.getCommentId());

        //DatabaseReference ref = database.getReference("albums").child(albumId).
        DatabaseReference ref = database.getReference("images").child(albumId).child(image.getImageId());

        ref.setValue(json, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("TAG", "Error: Image could not be saved "
                            + databaseError.getMessage());
                    listener.onCompletion(false);
                } else {
                    Log.e("TAG", "Success : Image saved successfully.");
                    listener.onCompletion(true);

                }
            }
        });
        //myRef.child(employee.id).setValue(json);
    }

    public static void removeImage(String albumId,String imageId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("images").child(albumId).child(imageId);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    snap.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });
    }

}
