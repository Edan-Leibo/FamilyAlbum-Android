package com.example.adima.familyalbumproject.Model.Entities.Family;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.adima.familyalbumproject.Model.Entities.Album.Album;

/**
 * Created by adima on 05/03/2018.
 */
/*
Interaction between family to firebase
 */
public class FamiliesFirebase {
    public interface GetKeyListener{
        public void onCompletion(String success);
    }

    /**
     * Add a new familt to firebase
     * @param listener
     */
    public static void addFamily(final GetKeyListener listener){
        Log.d("TAG", "add family to firebase");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String key = database.getReference("Families").push().getKey();
        DatabaseReference ref = database.getReference("families").child(key);
        DatabaseReference ref2 = database.getReference("albums").child(key).child("Dummy AlbumId");
        Album album = new Album("Dummy AlbumId" ,"Dummy name", "Dummy date","Dummy location","Dummy serial",-1);
        ref2.setValue(album.toJson(),null);
        ref.setValue(key, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError!=null){
                    listener.onCompletion(null);
                }
                else{
                    listener.onCompletion(key);
                }

            }
        });

    }

    /**
     * Remove a family from firebase
     * @param serialNumber
     */
    public static void removeFamily(String serialNumber) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("families").child(serialNumber);

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
