package com.example.adima.familyalbumproject.Model.SQL;

import android.util.Log;

import com.example.adima.familyalbumproject.Model.Entities.Album.Album;
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

public class AlbumFirebase {
    AlbumFirebase(){

    }


    public interface Callback<T>{
        void onComplete(T data);
    }

    public static void getAllAlbumsAndObserve(String serialNumber,final Callback<List<Album>> callback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("albums").child(serialNumber);


        ValueEventListener listener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Album> list = new LinkedList<Album>();

                for(DataSnapshot snap:dataSnapshot.getChildren()){

                    Album album = snap.getValue(Album.class);

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

    public static void getAllAlbumsAndObserve(String serialNumber,long lastUpdate,final Callback<List<Album>> callback){
        Log.d("TAG", "getAllCommentsAndObserve " + lastUpdate);
        Log.d("TAG", "getAllCommentsAndObserve");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("comments");
        DatabaseReference myRef = database.getReference("albums").child(serialNumber);

        Query query = myRef.orderByChild("lastUpdated").startAt(lastUpdate);
        Log.d("TAG","the query is ok");

        ValueEventListener listener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TAG","the data changed");
                List<Album> list = new LinkedList<Album>();

                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Log.d("TAG","got the children");

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

    public interface OnCreationAlbum{
        public void onCompletion(boolean success);
    }

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



    public static void removeAlbum(Album album, final ModelFirebase.OnRemove listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("albums").child(album.getSerialNumber()).child(album.getAlbumId());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    snap.getRef().removeValue(new DatabaseReference.CompletionListener() {
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

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });


    }
/*

    class MyDelete extends AsyncTask<Album, String, Boolean> {

        @Override
        protected Boolean doInBackground(Album... albums) {
            Log.d("TAG", "starting delte from local storage in thread");
            if (albums != null) {
                for (Album album : albums) {
                    Log.d("TAG", "the name of the album is:" + album.getName());
                    Log.d("TAG", "the id of the album is:" + album.getAlbumId());
                    AppLocalStore.db.albumDao().delete(album);
                }

            }
            return true;

        }
    }

    public void removeFromLocalDb(Album album) {
        MyDelete delete = new MyDelete();
        delete.execute(album);

    }

    public interface Callback<T> {
        void onComplete(T data);
    }

    public static void listenToDel(DatabaseReference myRef) {
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("TAG", "the child removed**************");
                Album album = dataSnapshot.getValue(Album.class);
                Log.d("TAG", "the name of the child is:" + album.getAlbumId());


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public interface OnAlbumEvent {
        public void OnCompletion(Album album,String event);
    }


    public static void getAllAlbumsAndObserve(String serialNumber,final Callback<List<Album>> callback) {
        Log.d("TAG", "on get all albums and observe func in album firebase");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("albums").child(serialNumber);

        ValueEventListener listener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Album> list = new LinkedList<Album>();

                for(DataSnapshot snap:dataSnapshot.getChildren()){

                    Album album = snap.getValue(Album.class);

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


    public static void getAllAlbumsAndObserve(String serialNumber, long lastUpdate, final Callback<List<Album>> callback) {

        Log.d("TAG", "getAllAlbumsAndObserve " + lastUpdate);
        Log.d("TAG", "getAllAlbumsAndObserve");


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("albums").child(serialNumber);
        Query query = myRef.orderByChild("lastUpdated").startAt(lastUpdate);
        Log.d("TAG","the query is ok");

        ValueEventListener listener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TAG","the data changed");
                List<Album> list = new LinkedList<Album>();

                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Log.d("TAG","got the children");

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



    public static void removeAlbum(Album album, final ModelFirebase.OnRemove listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("albums").child(album.getSerialNumber()).child(album.getAlbumId());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    snap.getRef().removeValue(new DatabaseReference.CompletionListener() {
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

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });


    }

*/

}
