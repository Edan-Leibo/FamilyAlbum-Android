package com.example.adima.familyalbumproject.Controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.adima.familyalbumproject.Controller.Album.AlbumFragment;
import com.example.adima.familyalbumproject.Controller.Album.CreateAlbumFragment;
import com.example.adima.familyalbumproject.Controller.Albums.AlbumsFragment;
import com.example.adima.familyalbumproject.Controller.Login.LoginFragment;
import com.example.adima.familyalbumproject.Entities.Album;
import com.example.adima.familyalbumproject.R;

import com.example.adima.familyalbumproject.Entities.Image;
import com.example.adima.familyalbumproject.Entities.Comment;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AlbumsFragment.OnFragmentInteractionListener {

    private List<Album> albums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fillAlbumsWithDummyData();
        showLoginFragment();
    }

    private void fillAlbumsWithDummyData() {
        albums = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            List<Image> images = new ArrayList<>();
            for (int j = 0; j < Math.random() * 20; j++) {
                images.add(new Image());
            }

            List<Comment> comments = new ArrayList<>();
            for (int k = 0; k < Math.random() * 20; k++) {
                comments.add(new Comment("Comment #" + k));
            }
            //albums.add(new Album("Album #" + i, "01.11.2011", images, comments));
            //String name,String date,String location,String serialNumber
            albums.add(new Album("Vacation","13.3.14","Eilat","15"));
        }
    }

    public void showLoginFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new LoginFragment())
                .commit();
    }

    public void showAlbumsFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, AlbumsFragment.newInstance(albums))
                .commit();
    }

    public void showCreateAlbumFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new CreateAlbumFragment())
                .commit();
    }


    public void showAlbumFragment() {
        showAlbumFragment(albums.get(0));
    }

    public void showAlbumFragment(Album album) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, AlbumFragment.newInstance(album))
                .commit();
    }

/*
    public void showCommentsFragment(Album album) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, CommentsFragment.newInstance(album))
                .commit();
    }*/



    @Override
    public void onItemSelected(Album album) {

    }/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // Album album= new Album();
       // album.date="17.04.15";
       // album.serialNumber="20";
       // album.location="U.S.A";
       // album.name="Netamel";

      //  AlbumFirebase.addAlbum(album);

/*
        LiveData<List<Album>> albums = AlbumFirebase.instance.getAlbumsList();
        for(int i=0;i<albums.getValue().size();i++){
            Log.d("TAG", albums.getValue().get(i).name);
       }


*/

/*
        ModelFirebase firebase = new ModelFirebase();
        firebase.getAllAlbumsAndObserve(new ModelFirebase.GetAllAlbumsAndObserveCallback() {
            @Override
            public void onComplete(List<Album> list) {
                Log.d("TAG","got list in the model");
                //callback.onComplete(list);
            }

            @Override
            public void onCancel() {
                //callback.onCancel();
            }
        });


    }
    */
}
