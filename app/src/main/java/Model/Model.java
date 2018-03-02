package Model;

import android.util.Log;

import java.util.List;

import Model.Firebase.DatabaseFirebase;
import Model.Firebase.ModelFirebase;

/**
 * Created by adima on 01/03/2018.
 */

public class Model {
    private static Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();


    private Model() {


    }

    public static Model instance() {
        return instance;
    }

    public void addAlbum(Album album) {
        modelFirebase.addAlbum(album);

    }

    public void getAlbums() {
       this.modelFirebase.databaseFirebase.getAlbums(new DatabaseFirebase.GetAlbumsListener() {
            @Override
            public void onComplete(List<Album> albums) {
                    Log.d("TAG", "got all the albums in the model");
            }
        });



    }
}