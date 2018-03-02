package Model.Firebase;

import android.util.Log;

import java.util.List;

import Model.Album;

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
    public void getAlbums(DatabaseFirebase.GetAlbumsListener tag) {
        this.databaseFirebase.getAlbums(new DatabaseFirebase.GetAlbumsListener() {
            @Override
            public void onComplete(List<Album> studentList) {

                    Log.d("TAG", "all the albums are in model fire base" );

            }
        });
    }


    }
