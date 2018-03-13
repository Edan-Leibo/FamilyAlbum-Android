package com.example.adima.familyalbumproject.Model.Entities.Album;

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
 * Created by adima on 02/03/2018.
 */
/*
Interaction between album to firebase
 */
public class AlbumFirebase {

    private static ChildEventListener deleteListener;
    private static ValueEventListener changesListener;
    private static Query query;
    private static DatabaseReference myRef;

    AlbumFirebase() {
    }



    public interface CallbackOnCommentUpdate<Album> {
        void onDeleted(Album data);
        void dataChanged(List<Album> list);
    }


    public static void observeAllAlbums(String serialNumber, long lastUpdate, final CallbackOnCommentUpdate<Album> callback) {

        myRef = FirebaseDatabase.getInstance().getReference("albums").child(serialNumber);
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
                callback.onDeleted(dataSnapshot.getValue(Album.class));
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
                List<Album> list = new LinkedList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Album album = snap.getValue(Album.class);
                    list.add(album);
                }
                callback.dataChanged(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        query.addValueEventListener(changesListener);
        myRef.addChildEventListener(deleteListener);
    }

    public static void removeAllObservers(){
        query.removeEventListener(changesListener);
        //myRef.removeEventListener(deleteListener);
    }

    public interface OnCreationAlbum {
        void onCompletion(boolean success);
    }



    /**
     * Add a comment to firebase
     * @param albumId
     * @param comment
     * @param listener
     */
    public static void addAlbum(String serialNumber, Album album, final OnCreationAlbum listener) {
        Log.d("TAG", "add album to firebase");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = database.getReference("albums").child(serialNumber).push().getKey();
        album.setAlbumId(key);
        HashMap<String, Object> json = album.toJson();
        json.put("lastUpdated", ServerValue.TIMESTAMP);
        Log.d("TAG", "the album id is:" + album.getAlbumId());
        DatabaseReference ref = database.getReference("albums").child(serialNumber).child(album.getAlbumId());
        ref.setValue(json, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("TAG", "Error: album could not be saved "
                            + databaseError.getMessage());
                    listener.onCompletion(false);
                } else {
                    Log.e("TAG", "Success : album saved successfully.");
                    listener.onCompletion(true);
                }
            }
        });

    }

    /**
     * Remove a comment from firebase
     * @param comment
     * @param listener
     */
    public static void removeAlbum(Album album, final ModelFirebase.OnRemove listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("albums").child(album.getSerialNumber()).child(album.getAlbumId());
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
