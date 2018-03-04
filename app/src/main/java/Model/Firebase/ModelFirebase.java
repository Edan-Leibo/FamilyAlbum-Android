package Model.Firebase;

import android.util.Log;

import com.example.adima.familyalbumproject.Album.Model.Album;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by adima on 01/03/2018.
 */

public class ModelFirebase {

   public DatabaseFirebase databaseFirebase;

    public ModelFirebase(){
    this.databaseFirebase = new DatabaseFirebase();

    }

    public void addAlbum(Album album){
        this.databaseFirebase.addAlbum(album);


    }

    public interface GetAllAlbumsAndObserveCallback {
        void onComplete(List<Album> list);
        void onCancel();
    }

    //work
    public void getAllAlbumsAndObserve(final GetAllAlbumsAndObserveCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("albums");
        ValueEventListener listener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Album> list = new LinkedList<Album>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
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
                callback.onCancel();
            }
        });
    }



    public void getAlbums(DatabaseFirebase.GetAlbumsListener tag) {
        this.databaseFirebase.getAlbums(new DatabaseFirebase.GetAlbumsListener() {
            @Override
            public void onComplete(List<Album> studentList) {
                Log.d("TAG", "all the albums are in model fire base" );

            }
        });
    }


    }
