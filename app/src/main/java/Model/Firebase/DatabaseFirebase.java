package Model.Firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Model.Entities.Album.Album;


/**
 * Created by adima on 01/03/2018.
 */

public class DatabaseFirebase {

    public FirebaseDatabase database;
    public String albumsPath = "albums";
    public DatabaseFirebase() {
        this.database = FirebaseDatabase.getInstance();
    }


    public interface GetAlbumsListener {
        void onComplete(List<Album> albumsList);
    }



    public void getAlbums(final GetAlbumsListener listener) {
        DatabaseReference myRef = database.getReference(albumsPath);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LinkedList<Album> data = new LinkedList<>();

                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Map<String, Object> value = (Map<String, Object>) snap.getValue();
                    Album album= new Album(value);
                    Log.d("TAG", "the name of the album is:" + album.name);
                    Log.d("TAG", "the id of the album is:" + album.albumId);
                    Log.d("TAG", "the location of the album is:" + album.location);
                    Log.d("TAG", "the date of the album is:" + album.date);
                    Log.d("TAG", "the serial number of the albums is:" + album.serialNumber);
                    data.add(album);
                }
                listener.onComplete(data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onComplete(null);
            }
        });
    }


}
