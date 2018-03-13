package com.example.adima.familyalbumproject.Model.Entities.Image;

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
 * Created by adima on 05/03/2018.
 */
/*

Interaction between image to firebase

 */
public class ImageFirebase {


    private static ChildEventListener deleteListener;
    private static ValueEventListener changesListener;
    private static Query query;
    private static DatabaseReference myRef;

    ImageFirebase() {
    }



    public interface CallbackOnImageUpdate<Image> {
        void onDeleted(Image image);
        void dataChanged(List<Image> list);
    }

    /**
     * Get all images from firebase according to the id of an album and the last update date
     * @param albumId
     * @param lastUpdate
     * @param callback
     */

    public static void observeAllImages(String albumId, long lastUpdate, final CallbackOnImageUpdate<Image> callback) {

        myRef = FirebaseDatabase.getInstance().getReference("images").child(albumId);
        query = myRef.orderByChild("lastUpdated").startAt(lastUpdate);

        deleteListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                callback.onDeleted(dataSnapshot.getValue(Image.class));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        changesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Image> list = new LinkedList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Image image = snap.getValue(Image.class);
                    list.add(image);
                }
                callback.dataChanged(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        query.addValueEventListener(changesListener);

        myRef.removeEventListener(deleteListener);
        myRef.addChildEventListener(deleteListener);
    }

    public static void removeAllObservers(){
        query.removeEventListener(changesListener);
    }

    public interface OnCreationImage {
        void onCompletion(boolean success);
    }


    /**
     * Add an image to firebase
     * @param albumId
     * @param image
     * @param listener
     */
    public static void addImage(String albumId, Image image, final OnCreationImage listener) {
        Log.d("TAG", "add image to firebase");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = database.getReference("images").child(albumId).push().getKey();
        image.setImageId(key);
        HashMap<String, Object> json = image.toJson();
        json.put("lastUpdated", ServerValue.TIMESTAMP);
        Log.d("TAG", "the image id is:" + image.getImageId());
        DatabaseReference ref = database.getReference("images").child(albumId).child(image.getImageId());
        ref.setValue(json, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("TAG", "Error: image could not be saved "
                            + databaseError.getMessage());
                    listener.onCompletion(false);
                } else {
                    Log.e("TAG", "Success : imageg saved successfully.");
                    listener.onCompletion(true);
                }
            }
        });

    }

    /**
     * Remove an image from firebase
     * @param image
     * @param listener
     */
    public static void removeImage(Image image, final ModelFirebase.OnRemove listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("images").child(image.getAlbumId()).child(image.getImageId());
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


