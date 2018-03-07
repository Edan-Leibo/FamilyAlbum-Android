package Model.SQL;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import Model.Entities.User.User;
import Model.Firebase.FirebaseAuthentication;
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

        String emailUser = FirebaseAuthentication.getUserEmail().replaceAll("[^A-Za-z]+", "");;


        DatabaseReference ref= database.getReference("usersProfiles").child(emailUser);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot==null) {
                    Log.d("TAG","data snap is null");
                    listener.onCompletion(null);

                }
                //for (DataSnapshot snap : dataSnapshot.getChildren()) {
                //String url = snap.getValue(String.class);
                else {
                    User user = dataSnapshot.getValue(User.class);
                    if(user==null){
                        listener.onCompletion(null);
                    }
                    else {
                        Log.d("TAG", "the user url is:" + user.getImageUrl());
                        //if (user.getImageUrl()==null)
                        listener.onCompletion(user.getImageUrl());
                    }
                }
                //}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });


    }

    public interface OnCreationUser{
        public void onCompletion(boolean success);
    }

    public static void addUserProfilePicture(User user, final OnCreationUser listener){
        Log.d("TAG", "add user profile picture to firebase");


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String emailUser = FirebaseAuthentication.getUserEmail().  replaceAll("[^A-Za-z]+", "");;
        DatabaseReference ref = database.getReference("usersProfiles").child(emailUser);



        HashMap<String, Object> json = user.toJson();

        //DatabaseReference ref = database.getReference("albums").child(albumId).

        ref.setValue(json, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("TAG", "Error: user could not be saved "
                            + databaseError.getMessage());
                    listener.onCompletion(false);
                } else {
                    Log.e("TAG", "Success : user saved successfully.");
                    listener.onCompletion(true);

                }
            }
        });
        //myRef.child(employee.id).setValue(json);
    }

    public static void removeUserImage() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userEmail = FirebaseAuthentication.getUserEmail().  replaceAll("[^A-Za-z]+", "");;
        DatabaseReference ref = database.getReference("usersProfiles").child(userEmail);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    dataSnapshot.getRef().removeValue();
                //}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });
    }

}
