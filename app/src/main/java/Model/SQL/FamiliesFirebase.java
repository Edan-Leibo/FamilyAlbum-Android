package Model.SQL;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.Entities.User.User;
import Model.Firebase.FirebaseAuthentication;

/**
 * Created by adima on 05/03/2018.
 */

public class FamiliesFirebase {
    public interface GetKeyListener{
        public void onCompletion(String success);
    }

    public static void getUserImageUrl(final GetKeyListener listener){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String emailUser = FirebaseAuthentication.getUserEmail();
        DatabaseReference ref= database.getReference("usersProfiles").child(emailUser);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        User user = snap.getValue(User.class);
                        listener.onCompletion(user.getImageUrl());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });

    }

    public static void addFamily(final GetKeyListener listener){
        Log.d("TAG", "add family to firebase");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String key = database.getReference("Families").push().getKey();
        DatabaseReference ref = database.getReference("families").child(key);

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
