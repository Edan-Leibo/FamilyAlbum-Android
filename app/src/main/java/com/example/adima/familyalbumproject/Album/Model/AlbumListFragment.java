package com.example.adima.familyalbumproject.Album.Model;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by adima on 02/03/2018.
 */

public class AlbumListFragment extends Fragment {
    //private onFragmentInteractionListener mListener;

    List<Album> albumsList = new LinkedList<>();
    //AlbumsListAdapter adapter;

    private AlbumsListViewModel albumsListViewModel;

    public AlbumListFragment(){

    }

    public static AlbumListFragment newInstance(){
       AlbumListFragment fragment = new AlbumListFragment();
       return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragament)
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
