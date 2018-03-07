package com.example.adima.familyalbumproject.Controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.adima.familyalbumproject.Controller.Album.AlbumFragment;
import com.example.adima.familyalbumproject.Controller.Album.CreateAlbumFragment;
import com.example.adima.familyalbumproject.Controller.Albums.AlbumsFragment;
import com.example.adima.familyalbumproject.Controller.Comments.CommentListFragment;
import com.example.adima.familyalbumproject.Controller.Login.LoginFragment;
import com.example.adima.familyalbumproject.R;

import Model.Entities.Album.Album;
import Model.Entities.Comment.Comment;

public class MainActivity extends AppCompatActivity implements AlbumsFragment.OnFragmentAlbumsInteractionListener,CommentListFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {




/*

        Image image = new Image();
        image.setAlbumId("-L6qPSRjtlvsIpxwffsp");
        image.setName("Adi");
        image.setImageUrl("gs://androidfamilyproject.appspot.com/harley_quinn.jpg");
Model.instance().addImage("-L6qPSRjtlvsIpxwffsp",image, new Model.OnCreation() {
    @Override
    public void onCompletion(boolean success) {

    }

});

    /*
        Album album= new Album();
        album.setLocation("Brazil");
        album.setName("Edan");
        album.setDate("30.3.18");
        album.setSerialNumber("-L6pJC5npal1cbekV32m");

            Model.instance().addAlbumToFirebase(album,"-L6pJC5npal1cbekV32m");
*/
    /*
Comment comment= new Comment();
comment.setUserId("Adi");
comment.setAlbumId("-L6qPSRjtlvsIpxwffsp");
comment.setText("I want to sleep");

Model.instance().addComment("-L6qPSRjtlvsIpxwffsp",comment);
*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*DELETE


        SharedPreferences ref =
                getSharedPreferences("familyInfo",MODE_PRIVATE);

        SharedPreferences.Editor ed = ref.edit();
        ed.putString("FAMILY_SERIAL", "-L6pJ7h5JSIjz-WQctTl");
        ed.commit();
        */
        showLoginFragment();
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
                .replace(R.id.container, AlbumsFragment.newInstance())
                .commit();
    }

    public void showCreateAlbumFragment() {
        getSupportFragmentManager()
                .beginTransaction()//.remove(getFragmentManager().getFragments())
                .replace(R.id.container, new CreateAlbumFragment())
                .commit();
    }


    public void showAlbumFragment() {
        showAlbumFragment("-L6lY_t5pKGBZTTzoeJ0");
    }

    public void showAlbumFragment(String albumID) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, AlbumFragment.newInstance(albumID))
                .commit();
    }


    public void showCommentsFragment(String albumId) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, CommentListFragment.newInstance(albumId))
                .commit();
    }


    @Override
    public void onItemSelected(Comment comment) {

    }
}