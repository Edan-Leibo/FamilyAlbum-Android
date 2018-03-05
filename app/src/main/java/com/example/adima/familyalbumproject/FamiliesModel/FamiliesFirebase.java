package com.example.adima.familyalbumproject.FamiliesModel;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by adima on 05/03/2018.
 */

public class FamiliesFirebase {

    public static String addFamily(){
        Log.d("TAG", "add family to firebase");


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String key = database.getReference("Families").push().getKey();


        //HashMap<String, Object> json = album.toJson();
        //json.put("lastUpdated", ServerValue.TIMESTAMP);

        //DatabaseReference myRef = database.getReference("albums");

        DatabaseReference ref = database.getReference("families").child(key);

        ref.setValue(key);
        return key;
        //myRef.child(employee.id).setValue(json);
    }







}
