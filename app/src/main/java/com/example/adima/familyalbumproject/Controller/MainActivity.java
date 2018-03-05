package com.example.adima.familyalbumproject.Controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.adima.familyalbumproject.Album.Model.Album;
import com.example.adima.familyalbumproject.Comment.Model.Comment;
import com.example.adima.familyalbumproject.Controller.Album.AlbumFragment;
import com.example.adima.familyalbumproject.Controller.Album.CreateAlbumFragment;
import com.example.adima.familyalbumproject.Controller.Albums.AlbumsFragment;
import com.example.adima.familyalbumproject.Controller.Comments.CommentListFragment;
import com.example.adima.familyalbumproject.Controller.Login.LoginFragment;
import com.example.adima.familyalbumproject.R;

import java.util.List;

import Model.Model;

public class MainActivity extends AppCompatActivity implements AlbumsFragment.OnFragmentInteractionListener,CommentListFragment.OnFragmentInteractionListener {

    private List<Album> albums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*

        Model.instance().isFamilyExist("-L6pJ7h5JSIjz-WQctTl", new Model.IsFamilyExistCallback()  {
            @Override
            public void onComplete(boolean exist) {
                if(exist==true){
                    Log.d("TAG","the family exist in firebase");
                }
                else{
                    Log.d("TAG","the family is not exist in firebase");
                }
            }

            @Override
            public void onCancel() {

            }
        });

*/
         super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    public void onItemSelected(Album album) {

    }

    @Override
    public void onItemSelected(Comment comment) {

    }
}