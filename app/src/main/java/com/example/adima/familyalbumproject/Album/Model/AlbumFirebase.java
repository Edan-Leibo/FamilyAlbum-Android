package com.example.adima.familyalbumproject.Album.Model;

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
 * Created by adima on 02/03/2018.
 */

public class AlbumFirebase {
    public interface Callback<T>{
        void onComplete(T data);
    }

    public static void getAllAlbumsAndObserve(final Callback<List<Album>> callback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("albums");

        ValueEventListener listener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //List<Album> list = new LinkedList<Album>();

                List<Album> list = new LinkedList<Album>();



                for(DataSnapshot snap:dataSnapshot.getChildren()){

                    Album album = snap.getValue(Album.class);
                    Log.d("TAG",album.name);
                    list.add(album);
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

    public static void getAllAlbumsAndObserve(long lastUpdate,final Callback<List<Album>> callback){
        Log.d("TAG", "getAllAlbumsAndObserve " + lastUpdate);
        Log.d("TAG", "getAllAlbumsAndObserve");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("albums");

        Query query = myRef.orderByChild("lastUpdated").startAt(lastUpdate);
        Log.d("TAG","the query is ok");

        ValueEventListener listener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TAG","the data changed");

                List<Album> list = new LinkedList<Album>();

                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Log.d("TAG","got the children");
                    Log.d("TAG","got the children"+snap.toString());


                    Album album = snap.getValue(Album.class);
                        Log.d("TAG","got the data in Album repository"+album.name);
                        Log.d("TAG","got the data in Album repository"+album.location);
                        Log.d("TAG","got the data in Album repository"+album.serialNumber);

                        Log.d("TAG","got the data in Album repository"+album.date);

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


    public static void addAlbum(Album album){
        Log.d("TAG", "add album to firebase");


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String key = database.getReference("albums").push().getKey();


        album.albumId = key;

        HashMap<String, Object> json = album.toJson();
        json.put("lastUpdated", ServerValue.TIMESTAMP);

        //DatabaseReference myRef = database.getReference("albums");



        DatabaseReference ref = database.getReference("albums").child(album.albumId);

        ref.setValue(json);
        //myRef.child(employee.id).setValue(json);
    }




}
