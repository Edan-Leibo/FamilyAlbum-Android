package Model.SQL;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import Model.Entities.Album.Album;
import Model.Firebase.ModelFirebase;

/**
 * Created by adima on 02/03/2018.
 */

public class AlbumFirebase {


    class MyDelete extends AsyncTask<Album, String, Boolean> {


        @Override
        protected Boolean doInBackground(Album... albums) {
            Log.d("TAG", "starting delte from local storage in thread");
            if (albums != null) {

                //3. update the local DB

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


    public static void getAllAlbumsAndObserve(String serialNumber, final OnAlbumEvent listener) {
        Log.d("TAG", "on get all albums and observe func in album firebase");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("albums").child(serialNumber);

        ChildEventListener childListener = myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("TAG", "child was added");
                Album album = dataSnapshot.getValue(Album.class);
                Log.d("TAG", "the name of the child is:" + album.getAlbumId());
                listener.OnCompletion(album,"add");


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("TAG", "child was changed");
                //Album album = dataSnapshot.getValue(Album.class);
                String value = dataSnapshot.getValue().toString();
                Log.d("TAG", "value");
                //String val = dataSnapshot.get
                // Log.d("TAG", "the name of the child is:" + album.getAlbumId());
                // listener.OnCompletion(album,"del");

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("TAG", "child was added");
                Album album = dataSnapshot.getValue(Album.class);
                Log.d("TAG", "the name of the child is:" + album.getAlbumId());
                //listener.OnCompletion(album,"del",albu);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("albums").child(serialNumber);


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

        */
    }


    public static void getAllAlbumsAndObserve(String serialNumber, long lastUpdate, final OnAlbumEvent listener) {

        Log.d("TAG", "getAllAlbumsAndObserve " + lastUpdate);
        Log.d("TAG", "getAllAlbumsAndObserve");


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("albums").child(serialNumber);
/*
       myRef.orderByChild("lastUpdated").startAt(lastUpdate);
        Log.d("TAG", "the query is ok");

        //query.removeEventListener();

        //listenToDel(myRef);
        */
        ChildEventListener childListener = myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("TAG", "child was added");
                Album album = dataSnapshot.getValue(Album.class);
                Log.d("TAG", "the name of the child is:" + album.getAlbumId());
                listener.OnCompletion(album,"add");


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Album album = dataSnapshot.getValue(Album.class);
                Log.d("TAG", "THE child changed:" + album.getAlbumId());


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // if(dataSnapshot==null){
                // Log.d("TAG", "the snapshot is null");
                //  }
                // Log.d("TAG", "child was deleted");
                //Log.d("tag","the key"+dataSnapshot.getKey());

                String val = dataSnapshot.getKey();
                Log.d("TAG", "child was deleted");
                //String vali =dataSnapshot.
                Album album=new Album();
                album.setAlbumId(val);
                // Album album = dataSnapshot.getValue(Album.class);
                // Log.d("TAG", "the name of the child is:" + album.getAlbumId());
                listener.OnCompletion(album,"del");


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Album album = dataSnapshot.getValue(Album.class);
                Log.d("TAG", "THE child moved:" + album.getAlbumId());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

/*
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
        */
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

        //DatabaseReference myRef = database.getReference("albums");

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
        //ref.setValue(json);
        //myRef.child(employee.id).setValue(json);
    }

    public interface onRemove {
        public void onCompletion(boolean success);
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


}
