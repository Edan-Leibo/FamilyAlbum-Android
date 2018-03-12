package com.example.adima.familyalbumproject.Model.Entities.Album;

import android.util.Log;

import com.example.adima.familyalbumproject.Model.Firebase.ModelFirebase;
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
 * Created by adima on 02/03/2018.
 */
/*
Interaction between album to firebase
 */
public class AlbumFirebase {
    AlbumFirebase() {

    }

    public interface Callback<T> {
        void onComplete(T data);
    }

    /**
     * Get all the albums from firebase
     * @param serialNumber
     * @param callback
     */
    public static void getAllAlbumsAndObserve(String serialNumber, final Callback<List<Album>> callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("albums").child(serialNumber);
        ValueEventListener listener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Album> list = new LinkedList<Album>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Album album = snap.getValue(Album.class);
                    list.add(album);
                }
                callback.onComplete(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "error in db");
                callback.onComplete(null);
            }
        });
    }

    /**
     * Get all the albums from firebase according to their last update date
     * @param serialNumber
     * @param lastUpdate
     * @param callback
     */
    public static void getAllAlbumsAndObserve(String serialNumber, long lastUpdate, final Callback<List<Album>> callback) {
        Log.d("TAG", "getAllAlbumsAndObserve " + lastUpdate);
        Log.d("TAG", "getAllAlbumsAndObserve");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("albums").child(serialNumber);
        Query query = myRef.orderByChild("lastUpdated").startAt(lastUpdate);
        ValueEventListener listener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TAG", "the data changed");
                List<Album> list = new LinkedList<Album>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Album album = snap.getValue(Album.class);
                    list.add(album);
                }
                callback.onComplete(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onComplete(null);
            }
        });
    }

    public interface OnCreationAlbum {
        public void onCompletion(boolean success);
    }

    /**
     * Add album to firebase
     * @param album
     * @param serialNumber
     * @param listener
     */
    public static void addAlbum(Album album, String serialNumber, final OnCreationAlbum listener) {
        Log.d("TAG", "add album to firebase");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = database.getReference("albums").child(serialNumber).push().getKey();
        album.albumId = key;
        HashMap<String, Object> json = album.toJson();
        json.put("lastUpdated", ServerValue.TIMESTAMP);
        DatabaseReference ref = database.getReference("albums").child(serialNumber).child(album.albumId);
        ref.setValue(json, new DatabaseReference.CompletionListener() {


            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("TAG", "Error: Album could not be saved "
                            + databaseError.getMessage());
                    listener.onCompletion(false);
                } else {
                    listener.onCompletion(true);
                    Log.e("TAG", "Success : Album saved successfully.");

                }
            }
        });

    }

    /**
     * Remove album from firebase
     * @param album
     * @param listener
     */
    public static void removeAlbum(Album album, final ModelFirebase.OnRemove listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("albums").child(album.getSerialNumber()).child(album.getAlbumId());
        {
            ref.getRef().removeValue(new DatabaseReference.CompletionListener() {
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
}
