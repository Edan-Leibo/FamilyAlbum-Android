package com.example.adima.familyalbumproject.User;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import Model.Firebase.ModelFirebase;

/**
 * Created by adima on 06/03/2018.
 */

public class UserFirebase {

    public interface GetKeyListener{
        public void onCompletion(String success);
    }

    public static void getUserImageUrl(final ModelFirebase.GetKeyListener listener){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        //String emailUser = FirebaseAuthentication.getUserEmail();


        DatabaseReference ref= database.getReference("usersProfiles").child("adi");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    String url = snap.getValue(String.class);
                    listener.onCompletion(url);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });




    }


    public static void addUserProfilePicture(User user){
        Log.d("TAG", "add user profile picture to firebase");


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //String userName = FirebaseAuthentication.getUserEmail();
        DatabaseReference ref = database.getReference("usersProfiles").child("adi");



        HashMap<String, Object> json = user.toJson();

        //DatabaseReference ref = database.getReference("albums").child(albumId).

        ref.setValue(json, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("TAG", "Error: user could not be saved "
                            + databaseError.getMessage());
                } else {
                    Log.e("TAG", "Success : user saved successfully.");

                }
            }
        });
        //myRef.child(employee.id).setValue(json);
    }

    public static void removeUserImage() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //String userName = FirebaseAuthentication.getUserEmail();
        DatabaseReference ref = database.getReference("usersProfiles").child("adi");

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
