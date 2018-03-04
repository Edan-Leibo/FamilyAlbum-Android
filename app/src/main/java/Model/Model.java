package Model;

import android.util.Log;

import com.example.adima.familyalbumproject.Album.Model.Album;
import com.example.adima.familyalbumproject.MyApplication;

import java.util.List;

import Model.Firebase.DatabaseFirebase;
import Model.Firebase.ModelFirebase;
import Model.SQL.ModelSql;

/**
 * Created by adima on 01/03/2018.
 */

public class Model {

    private static Model instance = new Model();


    ModelFirebase modelFirebase;
    ModelSql modelSql;


    private Model() {
        this.modelFirebase  = new ModelFirebase();
        this.modelSql = new ModelSql(MyApplication.getMyContext());



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


    public interface GetAllAlbumsAndObserveCallback {
        void onComplete(List<Album> list);

        void onCancel();
    }

    public void getAllAlbumsAndObserve(final GetAllAlbumsAndObserveCallback callback) {
        //return StudentSql.getAllStudents(modelSql.getReadableDatabase());
        modelFirebase.getAllAlbumsAndObserve(new ModelFirebase.GetAllAlbumsAndObserveCallback() {
            @Override
            public void onComplete(List<Album> list) {
                callback.onComplete(list);
            }

            @Override
            public void onCancel() {
                callback.onCancel();
            }
        });

    }


}